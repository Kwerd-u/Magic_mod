package net.kwerdu.magicmod.event;

import net.kwerdu.magicmod.capability.PlayerManaProvider;
import net.kwerdu.magicmod.mechanics.mana.DefaultMana;
import net.kwerdu.magicmod.mechanics.mana.ManaUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = "magicmod", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InventoryEvents {
    private static int tickCounter = 0;

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && ++tickCounter % 5 == 0) {
            for (Player player : event.getServer().getPlayerList().getPlayers()) {
                player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
                    ((DefaultMana) mana).updateManaFromInventory(player);
                });
            }
        }
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        List<Component> tooltip = event.getToolTip();

        // Получаем текущую и максимальную Ману из предмета
        int currentMana = ManaUtils.getCurrentMana(stack);
        int maxMana = ManaUtils.getMaxMana(stack);

        // Добавляем информацию о Мане в подсказку, если она есть
        if (currentMana > 0 || maxMana > 0) {
            tooltip.add(Component.literal("§1Мана: " + currentMana + "/" + maxMana));
        }
    }
}
