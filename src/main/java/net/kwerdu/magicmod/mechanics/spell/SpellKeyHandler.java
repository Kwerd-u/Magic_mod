package net.kwerdu.magicmod.mechanics.spell;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = "magicmod", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpellKeyHandler {
    private static final int CAST_KEY = GLFW.GLFW_KEY_R;

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (event.getKey() == CAST_KEY && event.getAction() == GLFW.GLFW_PRESS) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                Spell spell = SpellRegistry.getSpell("test_spell");
                if (spell != null) {
                    spell.castSpellClientSide(player);
                }
            }
        }
    }
}
