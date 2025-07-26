package net.kwerdu.magicmod.network;

import net.kwerdu.magicmod.mechanics.mana.PlayerManaUtils;
import net.kwerdu.magicmod.mechanics.spell.*;
import net.kwerdu.magicmod.mechanics.spell.Runes.EffectContext;
import net.kwerdu.magicmod.mechanics.spell.Runes.EffectSystem;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class SpellHandler {
    public static void handleCastSpell(ServerPlayer player, String spellName) {
        Spell spell = SpellRegistry.getSpell(spellName);
        if (spell != null) {
            int totalCost = spell.getCost();
            int currentMana = PlayerManaUtils.getCurrentMana(player);

            if (totalCost <= currentMana) {
                PlayerManaUtils.spendMana(player, totalCost);

                List<Effect> effects = spell.getEffects();
                for (Effect effect : effects) {
                    EffectContext context = new EffectContext(player, player);
                    EffectSystem.applyEffects(effect, context);
                }
            }
        }
    }
}
