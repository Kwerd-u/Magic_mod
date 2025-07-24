package net.kwerdu.magicmod.mechanics.spell.Runes;

import net.kwerdu.magicmod.mechanics.spell.Runes.RuneInterfaces.Absorbtion;
import net.kwerdu.magicmod.mechanics.spell.TargetContext;
import net.minecraft.resources.ResourceLocation;

public class AbsorptionRune extends Rune{
    public AbsorptionRune() {
        super("Поглощение",
                new ResourceLocation("magicmod",
                "textures/rune/absorption_rune.png"),
                1,
                100,
                -0.3);
    }

    public void execute(TargetContext targetContext){
        if (nestedRune instanceof Absorbtion){
            ((Absorbtion) nestedRune).absorb(targetContext);
        }
    }

}
