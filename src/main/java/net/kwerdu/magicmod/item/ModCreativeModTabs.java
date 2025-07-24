package net.kwerdu.magicmod.item;

import net.kwerdu.magicmod.MagicMod;
import net.kwerdu.magicmod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MagicMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MAGIC_TAB = CREATIVE_MODE_TABS.register("magic_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.MAGICSHARD.get())) // Иконка вкладки
                    .title(Component.translatable("creativetab.magic_tab")) // Название вкладки
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.MAGICSHARD.get()); // Добавляем предметы в вкладку
                        output.accept(ModBlocks.MAGIC_SHARD_ORE.get());
                        output.accept(ModBlocks.MAGIC_SHARD_BLOCK.get());

                    })
                    .build());


    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
