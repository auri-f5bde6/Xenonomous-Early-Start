package net.hellomouse.xeno_early_start.client.block_entity

import net.hellomouse.xeno_early_start.block.block_entity.PrimitiveFireBlockEntity
import net.minecraft.block.CampfireBlock
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.util.math.RotationAxis
import net.minecraft.util.math.random.CheckedRandom
import net.minecraft.util.math.random.LocalRandom
import net.minecraft.util.math.random.Random
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class PrimitiveFireBlockEntityRenderer(ctx: BlockEntityRendererFactory.Context) : BlockEntityRenderer<PrimitiveFireBlockEntity> {
    private val itemRenderer: ItemRenderer = ctx.itemRenderer
    private val random = LocalRandom(12345);
    override fun render(
        arg: PrimitiveFireBlockEntity,
        f: Float,
        arg2: MatrixStack,
        arg3: VertexConsumerProvider,
        i: Int,
        j: Int
    ) {
        val direction = arg.cachedState.get(CampfireBlock.FACING)
        val defaultedList = arg.getItemsBeingCooked()
        val k = arg.getPos().asLong().toInt()

        for (l in defaultedList.indices) {
            val itemStack = defaultedList[l]
            if (itemStack != ItemStack.EMPTY) {
                arg2.push()
                arg2.translate(0.46875f, 0.2875f, 0.4531f)
                if (l==1){
                    arg2.translate(-0.2f,0f,0.2f)
                }
                arg2.scale(0.16f, 0.16f, 0.16f)
                arg2.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0f))
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
