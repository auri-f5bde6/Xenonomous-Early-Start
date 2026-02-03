package com.github.auri_f5bde6.xeno_early_start.client.entity

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.entity.ProwlerEntity
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.EntityModelLoader
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

class ProwlerEmissivityFeature(
    val context: FeatureRendererContext<ProwlerEntity, ProwlerEntityModel>,
    val loader: EntityModelLoader
) : FeatureRenderer<ProwlerEntity, ProwlerEntityModel>(
    context
) {
    companion object {
        val TEXTURE: Identifier = XenoEarlyStart.of("textures/entity/prowler/emissivity.png")
    }

    override fun render(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        entity: ProwlerEntity,
        limbAngle: Float,
        limbDistance: Float,
        tickDelta: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        if (!entity.isInvisible && entity.isIgnited) {
            renderModel(
                this.contextModel,
                TEXTURE,
                matrices,
                vertexConsumers,
                light,
                entity,
                1f,
                1f,
                1f
            )
        }
    }

}