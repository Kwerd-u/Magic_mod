package net.kwerdu.magicmod.mechanics.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.kwerdu.magicmod.mechanics.mana.DefaultMana;
import net.kwerdu.magicmod.mechanics.mana.ManaUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CheckItemManaCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("checkitemmana")
                .requires(source -> source.hasPermission(2)) // Только для операторов
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();
                            Player player = source.getPlayerOrException();
                            ItemStack heldItem = player.getMainHandItem();
                            player.displayClientMessage(Component.literal(String.valueOf(ManaUtils.getMaxMana(heldItem))), true);
                            return 1;
                        })
        );
    }
}

