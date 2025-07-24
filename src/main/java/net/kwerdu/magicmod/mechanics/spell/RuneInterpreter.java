package net.kwerdu.magicmod.mechanics.spell;

import net.kwerdu.magicmod.mechanics.spell.Runes.Rune;

import java.util.ArrayList;
import java.util.List;

public class RuneInterpreter {
    public List<Rune> interpret(List<Rune> runes_sequence){
        List<Rune> context_sequence = new ArrayList<>();
        int i = 0;
        while (runes_sequence.size() != i){
            Rune current_rune = runes_sequence.get(i);
            i++;
                if (current_rune.necessaryRunesAmount != 0){
                    Rune nested_rune = runes_sequence.get(i);
                    current_rune.fillWithRune(nested_rune);
                    i++;
                }
            context_sequence.add(current_rune);
        }
        return context_sequence;
    }
}
