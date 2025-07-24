package net.kwerdu.magicmod.capability;

import net.kwerdu.magicmod.mechanics.mana.DefaultMana;
import net.kwerdu.magicmod.mechanics.mana.IMana;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerManaProvider implements ICapabilitySerializable<CompoundTag> {
    public static final Capability<IMana> PLAYER_MANA = CapabilityManager.get(new CapabilityToken<>() {});

    private final IMana mana = new DefaultMana();
    private final LazyOptional<IMana> optional = LazyOptional.of(() -> mana);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return PLAYER_MANA.orEmpty(cap, optional);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("CurrentMana", mana.getCurrentMana());
        nbt.putInt("MaxMana", mana.getMaxMana());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        mana.setCurrentMana(nbt.getInt("CurrentMana"));
        mana.setMaxMana(nbt.getInt("MaxMana"));
    }
}
