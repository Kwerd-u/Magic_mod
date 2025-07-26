package net.kwerdu.magicmod.network.packet;

import net.kwerdu.magicmod.network.handler.SpellHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class SpellPacket {
    private final String spellName;
    private final UUID casterUUID;

    public SpellPacket(UUID casterUUID, String spellName) {
        this.casterUUID = casterUUID;
        this.spellName = spellName;
    }

    public SpellPacket(FriendlyByteBuf buf) {
        this.spellName = buf.readUtf();
        this.casterUUID = buf.readUUID();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(spellName);
        buf.writeUUID(casterUUID);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Находим игрока на сервере по UUID
            ServerPlayer caster = ServerLifecycleHooks.getCurrentServer()
                    .getPlayerList()
                    .getPlayer(casterUUID);
            if (caster != null) {
                SpellHandler.handleCastSpell(caster, spellName);
            } else {
                System.out.println("[SpellPacket] Игрок с UUID " + casterUUID + " не найден!");
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
