package com.github.auri_f5bde6.xeno_early_start.client.entity

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import net.minecraft.client.model.*
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity

class BrickEntityModel(root: ModelPart) : EntityModel<Entity>() {
    private val bbMain: ModelPart = root.getChild("bb_main")

    override fun setAngles(
        entity: Entity,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {
    }

    override fun render(
        matrices: MatrixStack?,
        vertexConsumer: VertexConsumer?,
        light: Int,
        overlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        bbMain.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha)
    }

    companion object {
        val LAYER_LOCATION: EntityModelLayer = EntityModelLayer(XenoEarlyStart.of("brick"), "main")
        val texturedModelData: TexturedModelData
            get() {
                val modelData = ModelData()
                val modelPartData = modelData.root
                val bbMain = modelPartData.addChild(
                    "bb_main",
                    ModelPartBuilder.create(),
                    ModelTransform.pivot(0.0f, 0.0f, 0.0f)
                )

                bbMain.addChild(
                    "cube_r1",
                    ModelPartBuilder.create().uv(0, 0).cuboid(-5.0f, -3.0f, -3.0f, 4.0f, 3.0f, 7.0f, Dilation(0.0f)),
                    ModelTransform.of(-4.0f, 0.0f, 3.0f, 0.0f, -1.5708f, 0.0f)
                )
                return TexturedModelData.of(modelData, 32, 32)
            }
    }
}
