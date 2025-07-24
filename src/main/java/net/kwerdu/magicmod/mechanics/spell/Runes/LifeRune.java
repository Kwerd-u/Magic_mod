package net.kwerdu.magicmod.mechanics.spell.Runes;

import net.kwerdu.magicmod.mechanics.spell.Runes.RuneInterfaces.Absorbtion;
import net.kwerdu.magicmod.mechanics.spell.Runes.RuneInterfaces.Recovery;
import net.kwerdu.magicmod.mechanics.spell.TargetContext;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class LifeRune extends Rune implements Absorbtion, Recovery {
    public LifeRune() {
        super("Жизнь",
                new ResourceLocation("magicmod", "textures/rune/life_rune.png"),
                0,
                100,
                0);
    }

    public void absorb(TargetContext target) {
        Entity entity = target.getTargetEntity();



        DamageSource damageSource = entity.level().damageSources().magic();
        if (entity instanceof LivingEntity && !entity.isInvulnerable()) {
            entity.hurt(damageSource, 5);
            }
    }

    @Override
    public void recovery(TargetContext target) {
        Entity entity = target.getTargetEntity();
        if (entity instanceof LivingEntity) {
            ((LivingEntity) entity).heal(5);
            }
    }

    public void execute(TargetContext targetContext){

    }
}
