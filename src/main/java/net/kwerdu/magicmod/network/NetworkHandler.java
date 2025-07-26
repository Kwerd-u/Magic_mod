package net.kwerdu.magicmod.network;

import net.kwerdu.magicmod.mechanics.mana.PlayerManaUtils;
import net.kwerdu.magicmod.network.packet.MagicCircleRenderPacket;
import net.kwerdu.magicmod.network.packet.RequestSyncManaPacket;
import net.kwerdu.magicmod.network.packet.SpellPacket;
import net.kwerdu.magicmod.network.packet.SyncManaPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1.0";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("magicmod", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int id = 0;

    public static void register() {
        // Регистрация пакета SyncManaPacket
        INSTANCE.registerMessage(id++, SyncManaPacket.class, SyncManaPacket::encode, SyncManaPacket::new, SyncManaPacket::handle);
        INSTANCE.registerMessage(id++, RequestSyncManaPacket.class, RequestSyncManaPacket::encode, RequestSyncManaPacket::new, RequestSyncManaPacket::handle);
        INSTANCE.registerMessage(id++, SpellPacket.class, SpellPacket::encode, SpellPacket::new, SpellPacket::handle);
        INSTANCE.registerMessage(id++, MagicCircleRenderPacket.class, MagicCircleRenderPacket::encode, MagicCircleRenderPacket::new, MagicCircleRenderPacket::handle);
    }



}
