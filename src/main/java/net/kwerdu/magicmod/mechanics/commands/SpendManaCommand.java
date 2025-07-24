package net.kwerdu.magicmod.mechanics.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.kwerdu.magicmod.mechanics.mana.PlayerManaUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class SpendManaCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("spendmana")
                .requires(source -> source.hasPermission(2)) // Только для операторов
                .then(Commands.argument("player", EntityArgument.player())
                .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                        .executes(context -> {
                            Player player = EntityArgument.getPlayer(context, "player");
                            int amount = IntegerArgumentType.getInteger(context, "amount");

                            PlayerManaUtils.spendMana(player, amount);
                            player.displayClientMessage(Component.literal("Потрачено " + amount + " маны!"), true);
                            return 1;
                        })
                )
                )
        );
    }
}
