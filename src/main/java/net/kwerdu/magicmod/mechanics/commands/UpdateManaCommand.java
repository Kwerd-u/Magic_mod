package net.kwerdu.magicmod.mechanics.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.kwerdu.magicmod.capability.PlayerManaProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.player.Player;

public class UpdateManaCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("updatemana")
                .requires(source -> source.hasPermission(2)) // Только для операторов
                .then(Commands.argument("player", EntityArgument.player())
                                .executes(context -> {
                                    Player player = EntityArgument.getPlayer(context, "player");

                                    player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
                                        mana.updateManaFromInventory(player);
                                    });


                                    return 1;
                                })
                )
        );
    }
}
