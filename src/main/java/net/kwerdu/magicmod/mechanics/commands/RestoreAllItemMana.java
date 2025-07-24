package net.kwerdu.magicmod.mechanics.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.kwerdu.magicmod.capability.PlayerManaProvider;
import net.kwerdu.magicmod.mechanics.mana.DefaultMana;
import net.kwerdu.magicmod.mechanics.mana.ManaUtils;
import net.kwerdu.magicmod.mechanics.mana.PlayerManaUtils;
import net.kwerdu.magicmod.network.NetworkHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class RestoreAllItemMana {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("restoreitemsmana")
                .requires(source -> source.hasPermission(2)) // Только для операторов
                .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> {
                            Player player = EntityArgument.getPlayer(context, "player");

                            if (player != null) {
                                // Восстановление маны в основных слотах инвентаря
                                for (ItemStack stack : player.getInventory().items) {
                                    if (!stack.isEmpty() && ManaUtils.getMaxMana(stack) > 0) {
                                        ManaUtils.restoreMana(stack);
                                    }
                                }

                                // Восстановление маны в броне
                                for (ItemStack stack : player.getInventory().armor) {
                                    if (!stack.isEmpty() && ManaUtils.getMaxMana(stack) > 0) {
                                        ManaUtils.restoreMana(stack);
                                    }
                                }

                                // Восстановление маны в оффхенде
                                ItemStack offHand = player.getInventory().offhand.get(0);
                                if (!offHand.isEmpty() && ManaUtils.getMaxMana(offHand) > 0) {
                                    ManaUtils.restoreMana(offHand);
                                }

                                player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
                                    mana.updateManaFromInventory(player);
                                });

                                }
                            else {
                                player.displayClientMessage(Component.literal("Плауер не обнаружен, система поиска пидорасов активирована"), true);
                            }

                            return 1; // Возвращаем 1, чтобы указать на успешное выполнение
                        }
                        )
                )
        );
    }
}
