package net.kwerdu.magicmod.mechanics.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.kwerdu.magicmod.capability.PlayerManaProvider;
import net.kwerdu.magicmod.mechanics.mana.DefaultMana;
import net.kwerdu.magicmod.mechanics.mana.ManaUtils;
import net.kwerdu.magicmod.network.NetworkHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class IncreaseItemMaxManaCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("increasemaxmana")
                .requires(source -> source.hasPermission(2)) // Только для операторов
                .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();
                            Player player = source.getPlayerOrException();
                            ItemStack heldItem = player.getMainHandItem();

                            int amount = IntegerArgumentType.getInteger(context, "amount");
                            ManaUtils.increaseMaxMana(heldItem, amount);
                            player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
                                mana.updateManaFromInventory(player);
                            });
                            player.displayClientMessage(Component.literal("Максимальная мана предмета увеличена на " + amount + "!"), true);
                            return 1;
                        })
                )
        );
    }
}

