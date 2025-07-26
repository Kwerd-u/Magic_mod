package net.kwerdu.magicmod.mechanics.spell;

import com.google.gson.JsonObject;
import net.kwerdu.magicmod.mechanics.mana.PlayerManaUtils;
import net.kwerdu.magicmod.mechanics.spell.Runes.Effect;
import net.kwerdu.magicmod.mechanics.spell.Runes.RuneLoader;
import net.kwerdu.magicmod.network.NetworkHandler;
import net.kwerdu.magicmod.network.packet.SpellPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Spell {
    private List<String> runeSequence;
    private List<Effect> effects;
    private List<ResourceLocation> runeTextures;
    private String name;
    private int cost;
    private SpellMagicCircleRenderer magicCircleRenderer;
    private RuneInterpreter interpreter;

    public Spell(String name, List<String> runeSequence) {
        this.name = name;
        this.runeSequence = runeSequence;
        magicCircleRenderer = new SpellMagicCircleRenderer();
        interpreter = new RuneInterpreter();
        effects = interpreter.interpret(runeSequence);
        runeTextures = loadTextures();
        cost = calculateTotalCost();
    }

    public void castSpellClientSide(Player player) {
        if (player.level().isClientSide && cost <= PlayerManaUtils.getCurrentMana(player)) {
                cast(player);
        }
    }

    public List<String> getRuneSequence() {
        return runeSequence;
    }
    public List<Effect> getEffects(){return effects;}
    public void cast(Player player) {
        SpellPacket packet = new SpellPacket(player.getUUID(), this.name);
        NetworkHandler.INSTANCE.sendToServer(packet);
    }

    private int calculateTotalCost() {
        int totalCost = 0;
        double totalModifier = 1;
        double totalAmplifier = 1.3;
        int additionalLevel = 0;
        boolean levelUp = false;
        JsonObject allRunes = RuneLoader.loadAllRunes();

        for (int i = 0; i < runeSequence.size(); i++){
            String[] parts = runeSequence.get(i).split(":", 2);
            String runeName = parts[0];
            int runeLevel = Integer.parseInt(parts[1]);

            JsonObject currentRune = allRunes.getAsJsonObject(runeName);

            if (currentRune.has("modifier")){
                if (currentRune.get("modifier").getAsString().equals("levelup")) {
                    additionalLevel+= runeLevel;
                    levelUp = true;
                }
            }

            if (levelUp){
                totalCost += (int)(currentRune.get("cost").getAsInt() * Math.pow(1.5, runeLevel));
                totalModifier *= Math.pow(currentRune.get("cost_modificator").getAsDouble(), runeLevel + 1);
                totalAmplifier += currentRune.get("amplify_modificator").getAsDouble();
                levelUp = false;
            }
            else {
                totalCost += (int)(currentRune.get("cost").getAsInt() * Math.pow(1.5, runeLevel + additionalLevel));
                if (currentRune.has("cost_modificator")) {
                    totalModifier *= Math.pow(currentRune.get("cost_modificator").getAsDouble(), runeLevel + 1 + additionalLevel);
                }
                if (currentRune.has("amplify_modificator")) {
                    totalAmplifier += currentRune.get("amplify_modificator").getAsDouble();
                }
                additionalLevel = 0;
            }

        }

        totalAmplifier = Math.max(1.05, totalAmplifier);
        totalModifier = Math.max(0.05, totalModifier);

        return (int) ((totalCost * (1 * totalModifier)) * Math.pow(totalAmplifier, runeSequence.size()));
    }


    private List<ResourceLocation> loadTextures() {
        JsonObject allRunes = RuneLoader.loadAllRunes();
        List<ResourceLocation> textures = new ArrayList<>();
        for (int i = 0; i < runeSequence.size(); i++){
            String[] parts = runeSequence.get(i).split(":", 2);
            String runeName = parts[0];
            JsonObject currentRune = allRunes.getAsJsonObject(runeName);

            textures.add(new ResourceLocation("magicmod", currentRune.get("texture").getAsString()));
        }
        return textures;
    }


    public String getName() {
        return name;
    }

    public int getCost() { return cost; }

    public List<ResourceLocation> getRuneTextures() { return runeTextures; }
}
