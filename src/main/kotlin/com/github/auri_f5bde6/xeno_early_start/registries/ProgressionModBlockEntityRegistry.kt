package com.github.auri_f5bde6.xeno_early_start.registries

import com.github.auri_f5bde6.xeno_early_start.ProgressionMod
import com.github.auri_f5bde6.xeno_early_start.block.block_entity.BrickFurnaceBlockEntity
import com.github.auri_f5bde6.xeno_early_start.block.block_entity.PrimitiveFireBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.BlockEntityType.BlockEntityFactory
import net.minecraft.util.math.BlockPos
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier

object ProgressionModBlockEntityRegistry {
    val DEF_REG: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ProgressionMod.MODID)

    @JvmField
    val BRICK_FURNACE: RegistryObject<BlockEntityType<BrickFurnaceBlockEntity>> =
        DEF_REG.register("brick_furnace", Supplier {
            BlockEntityType.Builder.create(BlockEntityFactory { pos: BlockPos, state: BlockState ->
                BrickFurnaceBlockEntity(
                    pos, state
                )
            }, ProgressionModBlockRegistry.BRICK_FURNACE.get()).build(null)
        })

    @JvmField
    val PRIMITIVE_FIRE: RegistryObject<BlockEntityType<PrimitiveFireBlockEntity>> =
        DEF_REG.register("primitve_fire", Supplier {
            BlockEntityType.Builder.create(BlockEntityFactory { pos: BlockPos, state: BlockState ->
                PrimitiveFireBlockEntity(
                    pos, state
                )
            }, ProgressionModBlockRegistry.PRIMITIVE_FIRE.get()).build(null)
        })
}
