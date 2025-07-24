package net.kwerdu.magicmod.mechanics.spell.Runes;

import net.kwerdu.magicmod.mechanics.spell.Runes.RuneInterfaces.Recovery;
import net.kwerdu.magicmod.mechanics.spell.TargetContext;
import net.minecraft.resources.ResourceLocation;

public class RecoveryRune extends Rune{
    public RecoveryRune() {
        super("Восстановление",
                new ResourceLocation("magicmod", "textures/rune/recovery_rune.png"),
                1,
                300,
                0.1);
    }

    public void execute(TargetContext targetContext){
        if (nestedRune instanceof Recovery){
            ((Recovery) nestedRune).recovery(targetContext);
        }
    }
}
