package net.kwerdu.magicmod.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RequestSyncManaPacket {
    public RequestSyncManaPacket() {
    }

    public RequestSyncManaPacket(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // Вызов метода синхронизации на сервере
            ServerPlayer player = context.getSender();
            player.sendSystemMessage(Component.literal("Тут могла быть ваша реклама"));
            if (player != null) {
                NetworkHandler.syncMana(player);
            }
        });
        context.setPacketHandled(true);
    }
}
