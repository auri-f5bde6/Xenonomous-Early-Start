package com.github.auri_f5bde6.xeno_early_start.client.entity

import com.github.auri_f5bde6.xeno_early_start.ProgressionMod
import com.github.auri_f5bde6.xeno_early_start.entity.BrickEntity
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class BrickEntityRenderer(arg: EntityRendererFactory.Context) :
    net.minecraft.client.render.entity.EntityRenderer<BrickEntity>(arg) {
    private val model: BrickEntityModel = BrickEntityModel(arg.getPart(BrickEntityModel.LAYER_LOCATION))

    override fun getTexture(entity: BrickEntity): Identifier {
        return TEXTURE
    }

    override fun render(
        entity: BrickEntity,
        yaw: Float,
        tickDelta: Float,
        stack: MatrixStack,
        arg3: VertexConsumerProvider,
        lightLevel: Int
    ) {
        stack.push()

        val vertexConsumer =
            ItemRenderer.getDirectItemGlintConsumer(arg3, this.model.getLayer(this.getTexture(entity)), false, false)
        this.model.render(stack, vertexConsumer, lightLevel, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f)
        stack.pop()
        super.render(entity, yaw, yaw, stack, arg3, lightLevel)
    }

    companion object {
        val TEXTURE: Identifier = ProgressionMod.of("textures/entity/brick.png")
    }
}
