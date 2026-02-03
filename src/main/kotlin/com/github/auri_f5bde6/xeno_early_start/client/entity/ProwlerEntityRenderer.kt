package com.github.auri_f5bde6.xeno_early_start.client.entity

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.entity.ProwlerEntity
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class ProwlerEntityRenderer : MobEntityRenderer<ProwlerEntity, ProwlerEntityModel> {

    constructor(arg: EntityRendererFactory.Context) : super(
        arg,
        ProwlerEntityModel(arg.getPart(ProwlerEntityModel.LAYER_LOCATION)), 0.5f
    ) {
        this.addFeature(ProwlerEmissivityFeature(this, arg.modelLoader))
    }

    companion object {
        @JvmField
        val TEXTURE: Identifier = XenoEarlyStart.of("textures/entity/prowler/prowler.png")
    }

    // Taken from CreeperEntityRenderer
    override fun scale(arg: ProwlerEntity, arg2: MatrixStack, f: Float) {
        var g = arg.getClientFuseTime(f)
        val h = 1.0f + MathHelper.sin(g * 100.0f) * g * 0.01f
        g = MathHelper.clamp(g, 0.0f, 1.0f)
        g *= g
        g *= g
        val i = (1.0f + g * 0.4f) * h
        val j = (1.0f + g * 0.1f) / h
        arg2.scale(i, j, i)
    }

    // Taken from CreeperEntityRenderer
    override fun getAnimationCounter(arg: ProwlerEntity, f: Float): Float {
        val g = arg.getClientFuseTime(f)
        return if ((g * 10.0f).toInt() % 2 == 0) 0.0f else MathHelper.clamp(g, 0.5f, 1.0f)
    }

    override fun getTexture(arg: ProwlerEntity): Identifier {
        return TEXTURE
    }
}