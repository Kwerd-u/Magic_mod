package net.kwerdu.magicmod.mechanics.spell.Runes;

import net.kwerdu.magicmod.mechanics.spell.TargetContext;
import net.minecraft.resources.ResourceLocation;

public abstract class Rune {
    public String name;
    ResourceLocation texture;
    public int necessaryRunesAmount;
    boolean isFilled;
    public Rune nestedRune; // Вложенная руна (если требуется)
    public RuneTypes type = RuneTypes.Undefined;
    public int cost;
    public double costModifier;


    public Rune(String name, ResourceLocation texture, int necessaryRunesAmount, int cost, double costModifier) {
        this.name = name;
        this.texture = texture;
        this.necessaryRunesAmount = necessaryRunesAmount;
        this.isFilled = necessaryRunesAmount == 0;
        this.cost = cost;
        this.costModifier = costModifier;
    }

    public void fillWithRune(Rune rune) {
        isFilled = true;
        nestedRune = rune;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public abstract void execute(TargetContext targetContext);
}
