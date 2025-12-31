package com.github.auri_f5bde6.xeno_early_start.client.block_entity

import com.github.auri_f5bde6.xeno_early_start.block.PrimitiveFireBlock
import com.github.auri_f5bde6.xeno_early_start.block.block_entity.PrimitiveFireBlockEntity
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.util.math.RotationAxis
import net.minecraft.util.math.random.LocalRandom
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class PrimitiveFireBlockEntityRenderer(ctx: BlockEntityRendererFactory.Context) : BlockEntityRenderer<PrimitiveFireBlockEntity> {
    private val itemRenderer: ItemRenderer = ctx.itemRenderer
    private val random = LocalRandom(12345)
    override fun render(
        arg: PrimitiveFireBlockEntity,
        f: Float,
        arg2: MatrixStack,
        arg3: VertexConsumerProvider,
        i: Int,
        j: Int
    ) {
        val direction = arg.cachedState.get(PrimitiveFireBlock.FACING)
        val defaultedList = arg.getItemsBeingCooked()
        val k = arg.getPos().asLong().toInt()

        for (l in defaultedList.indices) {
            val itemStack = defaultedList[l]
            if (itemStack != ItemStack.EMPTY) {
                arg2.push()
                arg2.translate(0.5f, 0.255f, 0.5f)
                arg2.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-direction.asRotation()))
                if (l == 0) {
                    arg2.translate(0.03f, 0f, 0.1f)
                    //arg2.translate(0.045f,0f,0.055f)
                } else {
                    arg2.translate(0.20f, 0f, -0.09f)
                }
                arg2.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0f))
                arg2.scale(0.25f, 0.25f, 0.25f)
                this.itemRenderer.renderItem(
                    itemStack,
                    ModelTransformationMode.FIXED,
                    i,
                    j,
                    arg2,
                    arg3,
                    arg.getWorld(),
                    k + l
                )
                arg2.pop()
            }
        }
    }

    companion object {
        private const val SCALE = 0.375f
    }
}
