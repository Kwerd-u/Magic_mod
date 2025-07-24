package net.kwerdu.magicmod.mechanics.spell;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.kwerdu.magicmod.MagicMod;
import net.kwerdu.magicmod.mechanics.spell.Runes.*;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpellRegistry {
    private static final Map<String, Rune> runeRegistry = new HashMap<>();
    private static final List<Spell> spells = new ArrayList<>();

    static {
        runeRegistry.put("absorption_rune", new AbsorptionRune());
        runeRegistry.put("recovery_rune", new RecoveryRune());
        runeRegistry.put("life_rune", new LifeRune());
        runeRegistry.put("durability_rune", new DurabilityRune());
    }

    private static List<Spell> loadSpells() {
        Gson gson = new Gson();
        try (InputStreamReader reader = new InputStreamReader(SpellRegistry.class.getResourceAsStream("/data/magicmod/spells.json"))) {
            List<SpellData> spellData = gson.fromJson(reader, new TypeToken<List<SpellData>>() {}.getType());
            List<Spell> spells = new ArrayList<>();
            for (SpellData data : spellData) {
                List<Rune> sequence = new ArrayList<>();
                for (String runeName : data.runes) {
                    sequence.add(runeRegistry.get(runeName));
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


