package net.kwerdu.magicmod.mechanics.mana;

import net.minecraft.world.entity.player.Player;

public interface IMana {
    int getCurrentMana();
    int getMaxMana();
    void setCurrentMana(int amount);
    void setMaxMana(int amount);
    void updateManaFromInventory(Player player);
    void spendManaFromInventory(Player player, int amount);
}
