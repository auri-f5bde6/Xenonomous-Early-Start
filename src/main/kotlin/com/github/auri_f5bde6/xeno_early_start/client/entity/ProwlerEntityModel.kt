package com.github.auri_f5bde6.xeno_early_start.client.entity

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import net.minecraft.client.model.*
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.client.render.entity.model.EntityModelPartNames
import net.minecraft.client.render.entity.model.SinglePartEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity


class ProwlerEntityModel<T : Entity>(var root: ModelPart) : SinglePartEntityModel<T>() {
    private val head: ModelPart = root.getChild(EntityModelPartNames.HEAD)
    private val body: ModelPart = root.getChild("body")
    private val feet: ModelPart = root.getChild("feet")

    override fun setAngles(
        entity: T,
        netHeadYaw: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
    }

    override fun render(
        matrices: MatrixStack,
        vertices: VertexConsumer,
        light: Int,
        overlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        head.render(matrices, vertices, light, overlay, red, green, blue, alpha)
        body.render(matrices, vertices, light, overlay, red, green, blue, alpha)
        feet.render(matrices, vertices, light, overlay, red, green, blue, alpha)
    }

    override fun getPart(): ModelPart {
        return root
    }


    companion object {
        val LAYER_LOCATION: EntityModelLayer = EntityModelLayer(XenoEarlyStart.of("prowler"), "main")
        val texturedModelData: TexturedModelData
            get() {

                val meshdefinition = ModelData()
                val partdefinition: ModelPartData = meshdefinition.root

                partdefinition.addChild(
                    EntityModelPartNames.HEAD,
                    ModelPartBuilder.create().uv(0, 0)
                        .cuboid(-4.0f, -30.0f, -4.0f, 8.0f, 8.0f, 8.0f, Dilation(0.0f)),
                    ModelTransform.pivot(0.0f, 24.0f, 0.0f)
                )

                partdefinition.addChild(
                    "body",
                    ModelPartBuilder.create().uv(0, 16)
                        .cuboid(-4.0f, -22.0f, -2.0f, 8.0f, 12.0f, 4.0f, Dilation(0.0f)),
                    ModelTransform.pivot(0.0f, 24.0f, 0.0f)
                )

                partdefinition.addChild(
                    "feet",
                    ModelPartBuilder.create().uv(24, 16)
                        .cuboid(-4.0f, -10.0f, -6.0f, 4.0f, 10.0f, 4.0f, Dilation(0.0f))
                        .uv(24, 30).cuboid(0.0f, -10.0f, -6.0f, 4.0f, 10.0f, 4.0f, Dilation(0.0f))
                        .uv(0, 32).cuboid(0.0f, -10.0f, 2.0f, 4.0f, 10.0f, 4.0f, Dilation(0.0f))
                        .uv(32, 0).cuboid(-4.0f, -10.0f, 2.0f, 4.0f, 10.0f, 4.0f, Dilation(0.0f)),
                    ModelTransform.pivot(0.0f, 24.0f, 0.0f)
                )

                return TexturedModelData.of(meshdefinition, 64, 64)
            }
    }
}