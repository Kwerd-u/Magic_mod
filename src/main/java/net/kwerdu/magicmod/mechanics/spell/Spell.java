package net.kwerdu.magicmod.mechanics.spell;

import net.kwerdu.magicmod.mechanics.mana.PlayerManaUtils;
import net.kwerdu.magicmod.network.NetworkHandler;
import net.kwerdu.magicmod.network.SpellPacket;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class Spell {
    private List<String> runeSequence;
    private List<Effect> effects;
    private String name;
    private int cost;
    private SpellMagicCircleRenderer magicCircleRenderer;
    private RuneInterpreter interpreter;

    public Spell(String name, List<String> runeSequence) {
        this.name = name;
        this.runeSequence = runeSequence;
        magicCircleRenderer = new SpellMagicCircleRenderer();
        interpreter = new RuneInterpreter();
        effects = interpreter.interpret(runeSequence);
        cost = 100;
    }

    public void castSpellClientSide(Player player) {
        if (player.level().isClientSide && cost <= PlayerManaUtils.getCurrentMana(player)) {
            // Клиентская логика
                cast(player);

        }
    }

    public List<String> getRuneSequence() {
        return runeSequence;
    }
    public List<Effect> getEffects(){return effects;}
    public void cast(Player player) {
        SpellPacket packet = new SpellPacket(player.getUUID(), this.name);
        NetworkHandler.INSTANCE.sendToServer(packet);
    }

//    private int calculateTotalCost() {
//        int totalCost = 0;
//        double totalModifier = 0;
//
//        for (Rune rune : runeSequence) {
//            totalCost += rune.cost;
//            totalModifier += rune.costModifier;
//        }
//
//        return (int) (totalCost * (1 + totalModifier));
//    }

    public String getName() {
        return name;
    }

    public int getCost(){return cost;}
}
