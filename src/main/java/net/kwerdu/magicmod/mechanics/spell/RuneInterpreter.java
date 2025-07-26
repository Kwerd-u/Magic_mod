package net.kwerdu.magicmod.mechanics.spell;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kwerdu.magicmod.mechanics.spell.Runes.RuneLoader;

import java.util.ArrayList;
import java.util.List;

public class RuneInterpreter {
    public List<Effect> interpret(List<String> rune_sequence){
        List<Effect> context_sequence = new ArrayList<>();
        JsonObject allRunes = RuneLoader.loadAllRunes();
        List<String> modifiers = new ArrayList<>();
        int additionalLevel = 0;

        int i = 0;
        while (i < rune_sequence.size()){
            String[] parts = rune_sequence.get(i).split(":", 2);

            String runeName = parts[0];
            int runeLevel = Integer.parseInt(parts[1]);

            JsonObject currentRune = allRunes.getAsJsonObject(runeName);
            Effect effect;

            if (currentRune.has("modifier")){
                String modifier = currentRune.get("modifier").getAsString();
                if (modifier.equals("levelup")){ additionalLevel += runeLevel; }
                else {modifiers.add(0, modifier);}
            }
            else if (currentRune.has("effects")){
                runeLevel += additionalLevel;
                JsonArray effects = currentRune.getAsJsonArray("effects");
                JsonObject effectObj = findCondition(effects, modifiers);
                effect = getEffect(effectObj, runeLevel);
                if (effect != null) {
                    context_sequence.add(effect);
                }
                additionalLevel = 0;
            }
            i++;
        }
        for (Effect effect : context_sequence){
            System.out.println(effect.effectType);
            System.out.println(effect.value);
        }
        return context_sequence;
    }

    private JsonObject findCondition(JsonArray effects, List<String> modifiers){
        JsonObject defaultEffect = null;

        for (JsonElement effectElement : effects){
            JsonObject effectObj = effectElement.getAsJsonObject();

            if (effectObj.has("condition")){
                String requiredModifier = effectObj.get("condition").getAsString();
                if (modifiers.contains(requiredModifier)){
                    return effectObj;
                }
            }
            else {
                defaultEffect = effectObj;
            }
        }

        return defaultEffect;
    }



    private Effect getEffect(JsonObject effectObj, int runeLevel){
        Effect effect = null;
        if (effectObj != null) {
            String effectType;
            String status = "";
            float value = 0;

            effectType = effectObj.get("type").getAsString();
            if (effectObj.has("status")){
                status = effectObj.get("status").getAsString();
            }
            for (String paramName : effectObj.keySet()) {
                if (!paramName.equals("type") && !paramName.equals("condition")) {
                    try {
                        value = (float) (effectObj.get(paramName).getAsFloat() * Math.pow(1.3, runeLevel));
                    } catch (Exception ignored) {
                    }
                }
            }

            if (status.equals("")) {
                effect = new Effect(effectType, value);
            }
            else {
                effect = new Effect(effectType, status, value);
            }
        }
        return effect;
    }
}
