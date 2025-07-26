package net.kwerdu.magicmod.mechanics.mana;

import net.kwerdu.magicmod.network.NetworkHandler;
import net.kwerdu.magicmod.network.handler.ManaSyncHandler;
import net.kwerdu.magicmod.network.packet.RequestSyncManaPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class DefaultMana implements IMana {
    private int currentMana = 0; // Текущее количество Маны
    private int maxMana = 0; // Максимальное количество Маны

    @Override
    public int getCurrentMana() {
        return currentMana;
    }

    @Override
    public int getMaxMana() {
        return maxMana;
    }

    @Override
    public void setCurrentMana(int amount) {
        this.currentMana = Math.max(0, amount); // Не допускаем отрицательных значений
    }

    @Override
    public void setMaxMana(int amount) {
        this.maxMana = Math.max(0, amount); // Не допускаем отрицательных значений
    }

    @Override
    public void updateManaFromInventory(Player player) {

        int totalCurrentMana = 0;
        int totalMaxMana = 0;

        // Суммируем Ману из всех предметов в инвентаре
        for (ItemStack stack : player.getInventory().items) {
            if (stack != null && !stack.isEmpty()) {
                totalCurrentMana += ManaUtils.getCurrentMana(stack);
                totalMaxMana += ManaUtils.getMaxMana(stack);
            }
        }

        // Суммируем Ману из экипировки
        for (ItemStack stack : player.getInventory().armor) {
            if (stack != null && !stack.isEmpty()) {
                totalCurrentMana += ManaUtils.getCurrentMana(stack);
                totalMaxMana += ManaUtils.getMaxMana(stack);
            }
        }

        // Суммируем Ману из оффхенда
        ItemStack offhand = player.getInventory().offhand.get(0);
        if (offhand != null && !offhand.isEmpty()) {
            totalCurrentMana += ManaUtils.getCurrentMana(offhand);
            totalMaxMana += ManaUtils.getMaxMana(offhand);
        }

        // Обновляем Ману игрока
        this.currentMana = totalCurrentMana;
        this.maxMana = totalMaxMana;
        Sync(player);
    }

    @Override
    public void spendManaFromInventory(Player player, int amount) {

        updateManaFromInventory(player);
        if (currentMana >= amount) {
            int remainingAmount = amount;

            // Тратим ману из предметов в инвентаре
            for (ItemStack stack : player.getInventory().items) {
                if (stack != null && !stack.isEmpty()) {
                    int stackMana = ManaUtils.getCurrentMana(stack);
                    if (stackMana > 0) {
                        int toSpend = Math.min(stackMana, remainingAmount);
                        ManaUtils.setCurrentMana(stack, stackMana - toSpend);
                        remainingAmount -= toSpend;
                        if (remainingAmount <= 0) {
                            break;
                        }
                    }
                }
            }

            // Тратим ману из экипировки
            for (ItemStack stack : player.getInventory().armor) {
                if (stack != null && !stack.isEmpty()) {
                    int stackMana = ManaUtils.getCurrentMana(stack);
                    if (stackMana > 0) {
                        int toSpend = Math.min(stackMana, remainingAmount);
                        ManaUtils.setCurrentMana(stack, stackMana - toSpend);
                        remainingAmount -= toSpend;
                        if (remainingAmount <= 0) {
                            break;
                        }
                    }
                }
            }

            // Тратим ману из оффхенда
            ItemStack offhand = player.getInventory().offhand.get(0);
            if (offhand != null && !offhand.isEmpty()) {
                int stackMana = ManaUtils.getCurrentMana(offhand);
                if (stackMana > 0) {
                    int toSpend = Math.min(stackMana, remainingAmount);
                    ManaUtils.setCurrentMana(offhand, stackMana - toSpend);
                    remainingAmount -= toSpend;
                }
            }

            // Обновляем общий резервуар маны
            this.currentMana -= amount;

            Sync(player);
        }
    }

    private void Sync(Player player){
        if (player.level().isClientSide) {
            RequestSyncManaPacket packet = new RequestSyncManaPacket();
            NetworkHandler.INSTANCE.sendToServer(packet);
        }
        else {
            ManaSyncHandler.syncMana(player);
        }
    }
}
