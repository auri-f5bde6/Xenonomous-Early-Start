package net.hellomouse.xeno_early_start.registries;

import net.hellomouse.xeno_early_start.ProgressionMod;
import net.hellomouse.xeno_early_start.block.BrickBlock;
import net.hellomouse.xeno_early_start.block.BrickFurnaceBlock;
import net.hellomouse.xeno_early_start.block.RawBrickBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.ToIntFunction;

public class ProgressionModBlockRegistry {
    public static final DeferredRegister<Block> DEF_REG = DeferredRegister.create(ForgeRegistries.BLOCKS, ProgressionMod.MODID);
    public static final RegistryObject<Block> RAW_BRICK = DEF_REG.register("raw_brick", () -> new RawBrickBlock(
            AbstractBlock.Settings.create()
                    .requiresTool()
                    .strength(5.0F, 6.0F)
                    .sounds(BlockSoundGroup.DEEPSLATE_BRICKS)
                    .ticksRandomly()
    ));
    public static final RegistryObject<Block> BRICK = DEF_REG.register("brick", () -> new BrickBlock(
            AbstractBlock.Settings.create()
                    .requiresTool()
                    .strength(5.0F, 6.0F)
                    .sounds(BlockSoundGroup.DEEPSLATE_BRICKS)
                    .ticksRandomly()
    ));
    public static final RegistryObject<Block> BRICK_FURNACE = DEF_REG.register("brick_furnace", () -> new BrickFurnaceBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_RED)
                    .instrument(Instrument.BASEDRUM)
                    .requiresTool()
                    .strength(3.5F)
                    .luminance(createLightLevelFromLitBlockState(13))

    ));

    private static ToIntFunction<BlockState> createLightLevelFromLitBlockState(int litLevel) {
        return arg -> arg.get(Properties.LIT) ? litLevel : 0;
    }
}
