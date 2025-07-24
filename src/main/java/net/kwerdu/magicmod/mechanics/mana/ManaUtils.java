package net.kwerdu.magicmod.mechanics.mana;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class ManaUtils {

    public static int getCurrentMana(ItemStack stack) {
        if (stack != null && stack.getTag() != null) {
            return stack.getTag().getInt("CurrentMana");
        }
        return 0;
    }

    public static int getMaxMana(ItemStack stack) {
        if (stack != null && stack.getTag() != null) {
            return stack.getTag().getInt("MaxMana");
        }
        return 0;
    }

    public static void setCurrentMana(ItemStack stack, int amount) {
        CompoundTag nbt = stack.getOrCreateTag();
        nbt.putInt("CurrentMana", Math.min(amount, getMaxMana(stack)));
    }

    public static void setMaxMana(ItemStack stack, int amount) {
        CompoundTag nbt = stack.getOrCreateTag();
        nbt.putInt("MaxMana", amount);
    }

    public static void increaseMaxMana(ItemStack stack, int amount){
        setMaxMana(stack, getMaxMana(stack) + amount);
    }

    public static void restoreMana(ItemStack stack){
        setCurrentMana(stack, getMaxMana(stack));
    }

}
