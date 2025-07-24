package net.kwerdu.magicmod.mechanics.spell.Runes;

import net.kwerdu.magicmod.mechanics.spell.Runes.RuneInterfaces.Absorbtion;
import net.kwerdu.magicmod.mechanics.spell.Runes.RuneInterfaces.Recovery;
import net.kwerdu.magicmod.mechanics.spell.TargetContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DurabilityRune extends Rune implements Absorbtion, Recovery {
    public DurabilityRune() {
        super("Долговечность",
                new ResourceLocation("magicmod", "textures/rune/durability_rune.png"),
                0,
                100,
                0
                );
    }

    private List<ItemStack> getDamageableItems(LivingEntity entity) {
        List<ItemStack> damageableItems = new ArrayList<>();
        entity.getArmorSlots().forEach(itemStack -> {
            if (!itemStack.isEmpty() && itemStack.isDamageableItem()) {
                damageableItems.add(itemStack);
            }
        });
        ItemStack offhandItem = entity.getOffhandItem();
        if (!offhandItem.isEmpty() && offhandItem.isDamageableItem()) {
            damageableItems.add(offhandItem);
        }
        ItemStack mainHandItem = entity.getMainHandItem();
        if (!mainHandItem.isEmpty() && mainHandItem.isDamageableItem()) {
            damageableItems.add(mainHandItem);
        }
        return damageableItems;
    }


    private List<ItemStack> getHurtedItems(LivingEntity entity) {
        List<ItemStack> damageableItems = getDamageableItems(entity);
        List<ItemStack> hurtedItems = new ArrayList<>();
        for (ItemStack itemStack : damageableItems) {
            if (itemStack.isDamaged()) {
                hurtedItems.add(itemStack);
            }
        }
        return hurtedItems;
    }


    @Override
    public void absorb(TargetContext target) {
        Entity entity = target.getTargetEntity();
        if (entity instanceof LivingEntity && !entity.level().isClientSide()) { // Проверка на сервер
            LivingEntity livingEntity = (LivingEntity) entity;
            List<ItemStack> damageableItems = getDamageableItems(livingEntity);
            if (!damageableItems.isEmpty()) {
                ItemStack itemStack = damageableItems.get(livingEntity.getRandom().nextInt(damageableItems.size()));
                if (itemStack != null && itemStack.isDamageableItem()) {
                    int currentDamage = itemStack.getDamageValue();
                    int maxDamage = itemStack.getMaxDamage();


                    // Ограничиваем урон
                    int damage = 100;

                    if (damage > 0) {
                        // Наносим урон
                        itemStack.hurtAndBreak(damage, livingEntity, (livingEntity_) -> {
                            itemStack.shrink(1); // Уменьшаем количество предметов на 1
                        });
                    }
                }
            }
        }
    }





    @Override
    public void recovery(TargetContext target) {
        Entity entity = target.getTargetEntity();
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            List<ItemStack> hurtedItems = getHurtedItems(livingEntity);
            if (!hurtedItems.isEmpty()) {
                ItemStack itemStack = hurtedItems.get(livingEntity.getRandom().nextInt(hurtedItems.size()));
                if (!itemStack.isEmpty() && itemStack.isDamageableItem()) {
                    int currentDamage = itemStack.getDamageValue();
                    int newDamage = Math.max(currentDamage - 100, 0);
                    itemStack.setDamageValue(newDamage);
                    resetItemState(itemStack);
                }
            }
        }
    }

    private void resetItemState(ItemStack itemStack) {
        CompoundTag nbt = itemStack.getOrCreateTag();
        nbt.remove("Locked"); // Удаляем метку "Locked", если она существует
    }



    public void execute(TargetContext targetContext){

    }
}
