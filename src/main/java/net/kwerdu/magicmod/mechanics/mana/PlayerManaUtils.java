package net.kwerdu.magicmod.mechanics.mana;

import net.kwerdu.magicmod.capability.PlayerManaProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class PlayerManaUtils {

    public static int getCurrentMana(Player player) {
        if (player.level().isClientSide) {
            return ClientManaData.getCurrentMana();
        }
        return player.getCapability(PlayerManaProvider.PLAYER_MANA)
                .map(IMana::getCurrentMana)
                .orElse(0);
    }



    public static int getMaxMana(Player player) {
        return player.getCapability(PlayerManaProvider.PLAYER_MANA).map(mana -> {
            return mana.getMaxMana();
        }).orElse(0);
    }

    public static boolean spendMana(Player player, int amount) {
        return player.getCapability(PlayerManaProvider.PLAYER_MANA).map(mana -> {
            if (getCurrentMana(player) >= amount){
                mana.spendManaFromInventory(player, amount);
                return true;
            } else {
                player.sendSystemMessage(Component.literal("Недостаточно Маны!"));
                return false;
            }
        }).orElse(false);
    }
}
