package net.kwerdu.magicmod;

import com.mojang.logging.LogUtils;
import net.kwerdu.magicmod.block.ModBlocks;
import net.kwerdu.magicmod.capability.PlayerManaProvider;
import net.kwerdu.magicmod.item.ModCreativeModTabs;
import net.kwerdu.magicmod.item.ModItems;
import net.kwerdu.magicmod.mechanics.commands.*;
import net.kwerdu.magicmod.mechanics.mana.DefaultMana;
import net.kwerdu.magicmod.mechanics.mana.PlayerManaUtils;
import net.kwerdu.magicmod.mechanics.spell.SpellMagicCircleRenderer;
import net.kwerdu.magicmod.mechanics.spell.SpellRegistry;
import net.kwerdu.magicmod.network.NetworkHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(MagicMod.MOD_ID)
public class MagicMod
{
    public static final String MOD_ID = "magicmod";
    public static final Logger LOGGER = LogUtils.getLogger();
    //public static final SpellMagicCircleRenderer renderer = new SpellMagicCircleRenderer();

    public MagicMod(FMLJavaModLoadingContext context)
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        IEventBus modEventBus = context.getModEventBus();

        ModCreativeModTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        SpellRegistry.init(); // Инициализация заклинаний

        modEventBus.addListener(this::commonSetup);


        //MinecraftForge.EVENT_BUS.register(renderer);
        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        AddManaToItemCommand.register(event.getDispatcher());
        SpendManaCommand.register(event.getDispatcher());
        IncreaseItemMaxManaCommand.register(event.getDispatcher());
        RestoreAllItemMana.register(event.getDispatcher());
        CastTestSpellCommand.register(event.getDispatcher());
        CheckItemManaCommand.register(event.getDispatcher());
        CheckPlayerMana.register(event.getDispatcher());
        SyncCommand.register(event.getDispatcher());
        UpdateManaCommand.register(event.getDispatcher());
    }

    private void setup(final FMLCommonSetupEvent event) {
        NetworkHandler.register();
    }


    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }
    }

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(new ResourceLocation("magicmod", "mana"), new PlayerManaProvider());
        }
    }

}
