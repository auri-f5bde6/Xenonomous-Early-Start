package net.hellomouse.progression_change.registries;

import net.hellomouse.progression_change.ProgressionMod;
import net.hellomouse.progression_change.block.BrickBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ProgressionModBlockRegistry {
    public static final DeferredRegister<Block> DEF_REG = DeferredRegister.create(ForgeRegistries.BLOCKS, ProgressionMod.MODID);
    public static final RegistryObject<Block> RAW_BRICK = DEF_REG.register("raw_brick", () -> new BrickBlock(
            AbstractBlock.Settings.create()
                    .requiresTool()
                    .strength(5.0F, 6.0F)
                    .sounds(BlockSoundGroup.DEEPSLATE_BRICKS)
                    .ticksRandomly()
    ) {
    });
}
