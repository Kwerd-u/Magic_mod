package net.kwerdu.magicmod.mechanics.mana;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class ClientManaData {
    private static int maxMana;
    private static int currentMana;


    public static void setMana(int maxMana, int currentMana) {
        ClientManaData.maxMana = maxMana;
        ClientManaData.currentMana = currentMana;
        }

    public static int getMaxMana() {
        return maxMana;
    }

    public static int getCurrentMana() {
        return currentMana;
    }
}
