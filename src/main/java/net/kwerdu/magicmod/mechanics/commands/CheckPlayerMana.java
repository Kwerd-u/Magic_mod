package net.kwerdu.magicmod.mechanics.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.kwerdu.magicmod.mechanics.mana.ManaUtils;
import net.kwerdu.magicmod.mechanics.mana.PlayerManaUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CheckPlayerMana {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("checkplayermana")
                .requires(source -> source.hasPermission(2)) // Только для операторов
                .then(Commands.argument("player", EntityArgument.player())
                .executes(context -> {
                    CommandSourceStack source = context.getSource();
                    Player player = EntityArgument.getPlayer(context, "player");

                    player.displayClientMessage(Component.literal(String.valueOf(PlayerManaUtils.getCurrentMana(player))), true);
                    return 1;
                })
                )
        );
    }
}

