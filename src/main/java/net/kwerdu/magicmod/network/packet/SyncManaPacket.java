package net.kwerdu.magicmod.network.packet;

import net.kwerdu.magicmod.mechanics.mana.ClientManaData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class SyncManaPacket {
    private int maxMana;
    private int currentMana;

    public SyncManaPacket(int maxMana, int currentMana) {
        this.maxMana = maxMana;
        this.currentMana = currentMana;
    }

    public SyncManaPacket(FriendlyByteBuf buf) {
        this.maxMana = buf.readInt();
        this.currentMana = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(maxMana);
        buf.writeInt(currentMana);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // Обновляем данные на клиенте
            Player player = Minecraft.getInstance().player;
            ClientManaData.setMana(maxMana, currentMana);
        });
        context.setPacketHandled(true);
    }
}
