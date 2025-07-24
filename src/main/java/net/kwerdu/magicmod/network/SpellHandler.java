package net.kwerdu.magicmod.network;

import net.kwerdu.magicmod.mechanics.mana.PlayerManaUtils;
import net.kwerdu.magicmod.mechanics.spell.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.kwerdu.magicmod.mechanics.spell.Runes.Rune;
import net.kwerdu.magicmod.mechanics.spell.Runes.RuneTypes;

import java.util.List;

public class SpellHandler {
    public static void handleCastSpell(ServerPlayer player, String spellName) {
        RuneInterpreter runeInterpreter = new RuneInterpreter();
        Spell spell = SpellRegistry.getSpell(spellName);
        if (spell != null) {

            int totalCost = spell.getCost();
            int currentMana = PlayerManaUtils.getCurrentMana(player);

            if (totalCost <= currentMana) {
                // Вычитаем ману из предметов
                PlayerManaUtils.spendMana(player, totalCost);

                // Интерпретируем последовательность рун и применяем заклинание
                List<Rune> interpretedSequence = runeInterpreter.interpret(spell.getRuneSequence());
                TargetContext targetContext = null;
                if (interpretedSequence.get(0).type != RuneTypes.Projectile) {
                    targetContext = new TargetContext(player);
                }

                for (Rune rune : interpretedSequence) {
                    if (rune.type != RuneTypes.Projectile) {
                        rune.execute(targetContext);
                    }
                }
            }
        }
    }
}
