package net.kwerdu.magicmod.mechanics.spell;

import net.kwerdu.magicmod.mechanics.mana.PlayerManaUtils;
import net.kwerdu.magicmod.mechanics.spell.Runes.Rune;
import net.kwerdu.magicmod.network.NetworkHandler;
import net.kwerdu.magicmod.network.SpellPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class Spell {
    private List<Rune> runeSequence;
    private String name;
    private int cost;
    SpellMagicCircleRenderer magicCircleRenderer;

    public Spell(String name, List<Rune> runeSequence) {
        this.name = name;
        this.runeSequence = runeSequence;
        magicCircleRenderer = new SpellMagicCircleRenderer();
        cost = calculateTotalCost();
    }

    public void castSpellClientSide(Player player) {
        if (player.level().isClientSide && cost <= PlayerManaUtils.getCurrentMana(player)) {
            // Клиентская логика
                cast(player);
                List<ResourceLocation> runeTextures = this.getRuneSequence().stream()
                        .map(Rune::getTexture)
                        .toList();

                // Показываем магический круг (только визуальный эффект)
                SpellMagicCircleRenderer.activate(
                        player.position(),
                        runeTextures
                );
        }
    }

    public List<Rune> getRuneSequence() {
        return runeSequence;
    }

    public void cast(Player player) {
        SpellPacket packet = new SpellPacket(player.getUUID(), this.name);
        NetworkHandler.INSTANCE.sendToServer(packet);
    }

    private int calculateTotalCost() {
        int totalCost = 0;
        double totalModifier = 0;

        for (Rune rune : runeSequence) {
            totalCost += rune.cost;
            totalModifier += rune.costModifier;
        }

        return (int) (totalCost * (1 + totalModifier));
    }

    public String getName() {
        return name;
    }

    public int getCost(){return cost;}
}
