package net.kwerdu.magicmod.network.handler;

import net.kwerdu.magicmod.mechanics.mana.PlayerManaUtils;
import net.kwerdu.magicmod.mechanics.spell.*;
import net.kwerdu.magicmod.mechanics.spell.Runes.Effect;
import net.kwerdu.magicmod.mechanics.spell.Runes.EffectContext;
import net.kwerdu.magicmod.mechanics.spell.Runes.EffectSystem;
import net.kwerdu.magicmod.network.NetworkHandler;
import net.kwerdu.magicmod.network.packet.MagicCircleRenderPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;

public class SpellHandler {
    public static void handleCastSpell(ServerPlayer player, String spellName) {
        Spell spell = SpellRegistry.getSpell(spellName);
        if (spell != null) {
            int totalCost = spell.getCost();
            int currentMana = PlayerManaUtils.getCurrentMana(player);
            if (totalCost <= currentMana) {
                PlayerManaUtils.spendMana(player, totalCost);

                List<ResourceLocation> runeTextures = spell.getRuneTextures();

                Vec3 position = player.position();

                PacketDistributor.PacketTarget target = PacketDistributor.NEAR.with(() ->
                        new PacketDistributor.TargetPoint(
                                position.x, position.y, position.z,
                                64, // радиус
                                player.level().dimension()
                        )
                );

                MagicCircleRenderPacket packet = new MagicCircleRenderPacket(position, runeTextures);

                NetworkHandler.INSTANCE.send(target, packet);


                List<Effect> effects = spell.getEffects();
                for (Effect effect : effects) {
                    EffectContext context = new EffectContext(player, player);
                    EffectSystem.applyEffects(effect, context);
                }
            }
        }
    }
}
