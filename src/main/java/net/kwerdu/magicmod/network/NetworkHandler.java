package net.kwerdu.magicmod.network;

import net.kwerdu.magicmod.mechanics.mana.PlayerManaUtils;
import net.minecraft.client.Minecraft;
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
    }


    public static void sendToClient(SyncManaPacket packet, ServerPlayer player) {
        try {
            INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
        } catch (Exception e) {
            player.sendSystemMessage(Component.literal("Ошибка при отправке пакета: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    public static void requestSyncMana(Player player) {
        if (player.level().isClientSide()) {
            NetworkHandler.INSTANCE.sendToServer(new RequestSyncManaPacket());
        }
    }

    public static void syncMana(Player player) {
        if (!player.level().isClientSide()) {
            int maxMana = PlayerManaUtils.getMaxMana(player);
            int currentMana = PlayerManaUtils.getCurrentMana(player);

            NetworkHandler.sendToClient(new SyncManaPacket(maxMana, currentMana), (ServerPlayer) player);
        }
    }



}
