package net.kwerdu.magicmod.mechanics.spell;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.kwerdu.magicmod.MagicMod;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SpellRegistry {
    private static final List<Spell> spells = new ArrayList<>();

    private static List<Spell> loadSpells() {
        Gson gson = new Gson();
        try (InputStreamReader reader = new InputStreamReader(SpellRegistry.class.getResourceAsStream("/assets/magicmod/config/spells.json"))) {
            List<SpellData> spellData = gson.fromJson(reader, new TypeToken<List<SpellData>>() {}.getType());
            List<Spell> spells = new ArrayList<>();
            for (SpellData data : spellData) {
                List<String> sequence = new ArrayList<>();
                for (String runeName : data.runes) {
                    sequence.add(runeName);
                }
                spells.add(new Spell(data.name, sequence));
            }
            return spells;
        } catch (Exception e) {
            MagicMod.LOGGER.error("Failed to load spells from spells.json", e);
            return new ArrayList<>();
        }
    }

    public static void init() {
        spells.addAll(loadSpells());
    }

    public static Spell getSpell(String spellName) {
        Spell template = null;
        for (Spell spell : spells){
            if (spell.getName().equals(spellName)){
                template = spell;
                break;
            }
        }
        if (template == null) {
            throw new IllegalArgumentException("Заклинание с именем " + spellName + " не найдено.");
        }

        return new Spell(template.getName(), template.getRuneSequence());
    }

    private static class SpellData {
        String name;
        List<String> runes;
    }
}


