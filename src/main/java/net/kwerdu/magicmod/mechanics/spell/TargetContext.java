package net.kwerdu.magicmod.mechanics.spell;

import net.minecraft.world.entity.Entity;

public class TargetContext {
    private Entity entity;// Предмет, с которым нужно взаимодействовать

    public TargetContext(Entity entity) {
        this.entity = entity;
    }

    public Entity getTargetEntity(){
        return entity;
    }

}
