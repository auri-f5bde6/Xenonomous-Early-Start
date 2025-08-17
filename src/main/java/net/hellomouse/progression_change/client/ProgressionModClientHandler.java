package net.hellomouse.progression_change.client;

import me.shedaniel.autoconfig.AutoConfig;
import net.hellomouse.progression_change.ProgressionMod;
import net.hellomouse.progression_change.ProgressionModConfig;
import net.hellomouse.progression_change.ProgressionModEntityRegistry;
import net.hellomouse.progression_change.client.entity.BrickEntityModel;
import net.hellomouse.progression_change.client.entity.BrickEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ProgressionMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ProgressionModClientHandler {
    @SubscribeEvent
    public static void onFMLClientSetupEvent(FMLClientSetupEvent event) {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((mc, prevScreen) -> AutoConfig.getConfigScreen(ProgressionModConfig.class, prevScreen).get())
        );
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ProgressionModEntityRegistry.BRICK.get(), BrickEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BrickEntityModel.LAYER_LOCATION, BrickEntityModel::getTexturedModelData);
    }
}
