package net.hellomouse.xeno_early_start.client

import net.hellomouse.xeno_early_start.ProgressionMod
import net.hellomouse.xeno_early_start.ProgressionModConfig
import net.hellomouse.xeno_early_start.client.block_entity.PrimitiveFireBlockEntityRenderer
import net.hellomouse.xeno_early_start.client.entity.BrickEntityModel
import net.hellomouse.xeno_early_start.client.entity.BrickEntityRenderer
import net.hellomouse.xeno_early_start.client.screen.BrickFurnaceScreen
import net.hellomouse.xeno_early_start.client.screen.BrickFurnaceScreenHandler
import net.hellomouse.xeno_early_start.registries.ProgressionModBlockEntityRegistry
import net.hellomouse.xeno_early_start.registries.ProgressionModBlockRegistry
import net.hellomouse.xeno_early_start.registries.ProgressionModEntityRegistry
import net.hellomouse.xeno_early_start.registries.ProgressionModScreenHandlerRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.RenderLayers
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import java.util.function.Supplier

@EventBusSubscriber(value = [Dist.CLIENT], modid = ProgressionMod.Companion.MODID, bus = EventBusSubscriber.Bus.MOD)
object ProgressionModClientHandler {
    @SubscribeEvent
    fun onFMLClientSetupEvent(event: FMLClientSetupEvent) {
        event.enqueueWork(Runnable {
            HandledScreens.register(
                ProgressionModScreenHandlerRegistry.BRICK_FURNACE_SCREEN.get()
            ) { handler: BrickFurnaceScreenHandler, playerInventory: PlayerInventory, title: Text ->
                BrickFurnaceScreen(handler, playerInventory, title)
            }
        })
        BlockEntityRendererFactories.register(
            ProgressionModBlockEntityRegistry.PRIMITIVE_FIRE.get(), ::PrimitiveFireBlockEntityRenderer
        )
        RenderLayers.setRenderLayer(ProgressionModBlockRegistry.PRIMITIVE_FIRE.get(), RenderLayer.getCutout())
    }

    @SubscribeEvent
    fun registerRenderers(event: RegisterRenderers) {
        event.registerEntityRenderer(
            ProgressionModEntityRegistry.BRICK.get(),
            EntityRendererFactory { arg: EntityRendererFactory.Context -> BrickEntityRenderer(arg) })
    }

    @SubscribeEvent
    fun registerLayers(event: RegisterLayerDefinitions) {
        event.registerLayerDefinition(
            BrickEntityModel.Companion.LAYER_LOCATION,
            Supplier { BrickEntityModel.Companion.texturedModelData })
    }
}
