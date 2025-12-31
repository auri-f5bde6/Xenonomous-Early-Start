package com.github.auri_f5bde6.xeno_early_start.client

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.client.block_entity.PrimitiveFireBlockEntityRenderer
import com.github.auri_f5bde6.xeno_early_start.client.entity.BrickEntityModel
import com.github.auri_f5bde6.xeno_early_start.client.entity.BrickEntityRenderer
import com.github.auri_f5bde6.xeno_early_start.client.particle.CoalDustParticle
import com.github.auri_f5bde6.xeno_early_start.client.screen.BrickFurnaceScreen
import com.github.auri_f5bde6.xeno_early_start.client.screen.BrickFurnaceScreenHandler
import com.github.auri_f5bde6.xeno_early_start.registries.*
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.minecraft.client.particle.SpriteProvider
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.RenderLayers
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers
import net.minecraftforge.client.event.RegisterParticleProvidersEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import java.util.function.Supplier

@EventBusSubscriber(value = [Dist.CLIENT], modid = XenoEarlyStart.MODID, bus = EventBusSubscriber.Bus.MOD)
object XenoEarlyStartClientHandler {
    @SubscribeEvent
    fun onFMLClientSetupEvent(event: FMLClientSetupEvent) {
        event.enqueueWork(Runnable {
            HandledScreens.register(
                XenoEarlyStartScreenHandlerRegistry.BRICK_FURNACE_SCREEN.get()
            ) { handler: BrickFurnaceScreenHandler, playerInventory: PlayerInventory, title: Text ->
                BrickFurnaceScreen(handler, playerInventory, title)
            }
        })
        BlockEntityRendererFactories.register(
            XenoEarlyStartBlockEntityRegistry.PRIMITIVE_FIRE.get(), ::PrimitiveFireBlockEntityRenderer
        )
        RenderLayers.setRenderLayer(XenoEarlyStartBlockRegistry.PRIMITIVE_FIRE.get(), RenderLayer.getCutout())
    }

    @SubscribeEvent
    fun registerRenderers(event: RegisterRenderers) {
        event.registerEntityRenderer(
            XenoEarlyStartEntityRegistry.BRICK.get(),
            EntityRendererFactory { arg: EntityRendererFactory.Context -> BrickEntityRenderer(arg) })
    }

    @SubscribeEvent
    fun registerLayers(event: RegisterLayerDefinitions) {
        event.registerLayerDefinition(
            BrickEntityModel.LAYER_LOCATION,
            Supplier { BrickEntityModel.texturedModelData })
    }

    @SubscribeEvent
    fun registerParticles(event: RegisterParticleProvidersEvent) {
        event.registerSpriteSet(
            XenoEarlyStartParticleRegistry.COAL_DUST.get()
        ) { spriteProvider: SpriteProvider -> CoalDustParticle.Factory(spriteProvider) }
    }
}
