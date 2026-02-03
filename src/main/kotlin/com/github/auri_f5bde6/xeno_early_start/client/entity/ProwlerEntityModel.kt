package com.github.auri_f5bde6.xeno_early_start.client.entity

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.entity.ProwlerEntity
import net.minecraft.client.model.*
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.client.render.entity.model.EntityModelPartNames
import net.minecraft.client.render.entity.model.SinglePartEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.MathHelper
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn


@OnlyIn(Dist.CLIENT)
class ProwlerEntityModel(var root: ModelPart) : SinglePartEntityModel<ProwlerEntity>() {
    private val head: ModelPart = root.getChild(EntityModelPartNames.HEAD)
    private val body: ModelPart = root.getChild(EntityModelPartNames.BODY)
    private val leftFrontLeg: ModelPart = root.getChild(EntityModelPartNames.LEFT_FRONT_LEG)
    private val rightFrontLeg: ModelPart = root.getChild(EntityModelPartNames.RIGHT_FRONT_LEG)
    private val leftHindLeg: ModelPart = root.getChild(EntityModelPartNames.LEFT_HIND_LEG)
    private val rightHindLeg: ModelPart = root.getChild(EntityModelPartNames.RIGHT_HIND_LEG)


    // From CreeperEntityModel
    override fun setAngles(
        entity: ProwlerEntity,
        netHeadYaw: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        this.head.yaw = headYaw * (Math.PI / 180.0).toFloat()
        this.head.pitch = headPitch * (Math.PI / 180.0).toFloat()
        this.leftHindLeg.pitch = MathHelper.cos(netHeadYaw * 0.6662f) * 1.0f * limbDistance
        this.rightHindLeg.pitch = MathHelper.cos(netHeadYaw * 0.6662f + Math.PI.toFloat()) * 1.0f * limbDistance
        this.leftFrontLeg.pitch = MathHelper.cos(netHeadYaw * 0.6662f + Math.PI.toFloat()) * 1.0f * limbDistance
        this.rightFrontLeg.pitch = MathHelper.cos(netHeadYaw * 0.6662f) * 1.0f * limbDistance
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
        leftFrontLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha)
        rightFrontLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha)
        leftHindLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha)
        rightHindLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha)
    }

    override fun getPart(): ModelPart {
        return root
    }


    companion object {
        val LAYER_LOCATION: EntityModelLayer = EntityModelLayer(XenoEarlyStart.of("prowler"), "main")
        val texturedModelData: TexturedModelData
            get() {
                val modelData = ModelData()
                val modelPartData = modelData.root
                modelPartData.addChild(
                    EntityModelPartNames.HEAD,
                    ModelPartBuilder.create().uv(0, 0).cuboid(-4.0f, -30.0f, -4.0f, 8.0f, 8.0f, 8.0f, Dilation(0.0f)),
                    ModelTransform.pivot(0.0f, 24.0f, 0.0f)
                )

                modelPartData.addChild(
                    EntityModelPartNames.BODY,
                    ModelPartBuilder.create().uv(0, 16).cuboid(-4.0f, -22.0f, -2.0f, 8.0f, 12.0f, 4.0f, Dilation(0.0f)),
                    ModelTransform.pivot(0.0f, 24.0f, 0.0f)
                )

                modelPartData.addChild(
                    EntityModelPartNames.LEFT_FRONT_LEG,
                    ModelPartBuilder.create().uv(24, 30).cuboid(-2.0f, 0.0f, -4.0f, 4.0f, 10.0f, 4.0f, Dilation(0.0f)),
                    ModelTransform.pivot(2.0f, 14.0f, -2.0f)
                )

                modelPartData.addChild(
                    EntityModelPartNames.RIGHT_FRONT_LEG,
                    ModelPartBuilder.create().uv(24, 16).cuboid(-2.0f, 0.0f, -4.0f, 4.0f, 10.0f, 4.0f, Dilation(0.0f)),
                    ModelTransform.pivot(-2.0f, 14.0f, -2.0f)
                )

                modelPartData.addChild(
                    EntityModelPartNames.LEFT_HIND_LEG,
                    ModelPartBuilder.create().uv(0, 32).cuboid(-2.0f, 0.0f, 0.0f, 4.0f, 10.0f, 4.0f, Dilation(0.0f)),
                    ModelTransform.pivot(2.0f, 14.0f, 2.0f)
                )

                modelPartData.addChild(
                    EntityModelPartNames.RIGHT_HIND_LEG,
                    ModelPartBuilder.create().uv(32, 0).cuboid(-2.0f, 0.0f, 0.0f, 4.0f, 10.0f, 4.0f, Dilation(0.0f)),
                    ModelTransform.pivot(-2.0f, 14.0f, 2.0f)
                )
                return TexturedModelData.of(modelData, 64, 64)
            }
    }
}