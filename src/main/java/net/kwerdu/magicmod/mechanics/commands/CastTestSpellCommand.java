package net.kwerdu.magicmod.mechanics.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.kwerdu.magicmod.mechanics.spell.Spell;
import net.kwerdu.magicmod.mechanics.spell.SpellRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class CastTestSpellCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("casttestspell")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("number", IntegerArgumentType.integer(1, 2))// Только для операторов
                .executes(context -> {
                    CommandSourceStack source = context.getSource();
                    Player player = source.getPlayerOrException();
                    int number = IntegerArgumentType.getInteger(context, "number");

                    List<String> names = new ArrayList<>();
                    names.add("blood_sacrifice");
                    names.add("victim_of_strength");

                    // Получаем зарегистрированное заклинание
                    Spell testSpell = SpellRegistry.getSpell(names.get(number - 1));

                    if (testSpell != null) {
                        // Активируем заклинание
                        testSpell.cast(player);
                        // Сообщаем игроку об успешном применении заклинания
                             player.sendSystemMessage(Component.literal("Заклинание " + testSpell.getName() + " успешно применено!"));
                    } else {
                        player.sendSystemMessage(Component.literal("Заклинание не найдено!"));
                    }
                    testSpell = null;
                    return 1;
                })
                )
        );
    }
}
