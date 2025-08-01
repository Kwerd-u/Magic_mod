package net.kwerdu.magicmod.mechanics.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.kwerdu.magicmod.network.handler.ManaSyncHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.player.Player;

public class SyncCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("sync")
                .requires(source -> source.hasPermission(2)) // Только для операторов
                .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();
                            Player player = EntityArgument.getPlayer(context, "player");

                            ManaSyncHandler.syncMana(player);

                            return 1;
                        })
                )
        );
    }
}
