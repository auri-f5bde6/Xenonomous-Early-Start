package com.github.auri_f5bde6.xeno_early_start.registries

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.block.BrickBlock
import com.github.auri_f5bde6.xeno_early_start.block.BrickFurnaceBlock
import com.github.auri_f5bde6.xeno_early_start.block.PrimitiveFireBlock
import com.github.auri_f5bde6.xeno_early_start.block.RawBrickBlock
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.MapColor
import net.minecraft.block.enums.Instrument
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.state.property.Properties
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier
import java.util.function.ToIntFunction

object XenoEarlyStartBlockRegistry {
    val DEF_REG: DeferredRegister<Block> =
        DeferredRegister.create(ForgeRegistries.BLOCKS, XenoEarlyStart.MODID)

    @JvmField
    val RAW_BRICK: RegistryObject<Block> = DEF_REG.register("raw_brick", Supplier {
        RawBrickBlock(
            AbstractBlock.Settings.create()
                .mapColor(MapColor.GRAY)
                .strength(0.3f, 0.5f)
                .sounds(BlockSoundGroup.SLIME)
                .ticksRandomly()
        )
    })

    @JvmField
    val BRICK: RegistryObject<Block> = DEF_REG.register("brick", Supplier {
        BrickBlock(
            AbstractBlock.Settings.create()
                .mapColor(MapColor.DARK_RED)
                .strength(0.6f, 0.5f)
                .sounds(BlockSoundGroup.DEEPSLATE_BRICKS)
                .ticksRandomly()
        )
    })

    @JvmField
    val BRICK_FURNACE: RegistryObject<Block> = DEF_REG.register("brick_furnace", Supplier {
        BrickFurnaceBlock(
            AbstractBlock.Settings.create()
                .mapColor(MapColor.DARK_RED)
                .instrument(Instrument.BASEDRUM)
                .requiresTool()
                .strength(3.5f)
                .luminance(createLightLevelFromLitBlockState(13))

        )
    })

    @JvmField
    val PRIMITIVE_FIRE: RegistryObject<Block> = DEF_REG.register("primitive_fire", Supplier {
        PrimitiveFireBlock(
            1,
            AbstractBlock.Settings.create()
                .mapColor(MapColor.SPRUCE_BROWN)
                .instrument(Instrument.BASS)
                .strength(1f)
                .sounds(BlockSoundGroup.WOOD)
                .luminance(PrimitiveFireBlock.createLightLevel())
                .nonOpaque()
                .burnable()
        )
    })

    @JvmField
    val PLANT_FIBER_BLOCK: RegistryObject<Block> = DEF_REG.register("plant_fiber_block", Supplier {
        Block(
            AbstractBlock.Settings.create().mapColor(MapColor.DARK_GREEN).instrument(Instrument.BANJO).strength(0.5f)
                .sounds(BlockSoundGroup.GRASS)
        )
    })

    private fun createLightLevelFromLitBlockState(litLevel: Int): ToIntFunction<BlockState?> {
        return ToIntFunction { arg: BlockState? -> if (arg!!.get(Properties.LIT) ?: false) litLevel else 0 }
    }
}
