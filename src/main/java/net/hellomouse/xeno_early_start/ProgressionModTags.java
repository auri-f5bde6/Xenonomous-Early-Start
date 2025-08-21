package net.hellomouse.xeno_early_start;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

public class ProgressionModTags {
    public static class Blocks {
        public static final TagKey<Block> HAS_BLOCK_TO_BLOCK_RECIPE = createTag("has_block_to_block_recipe");

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ProgressionMod.of(name));
        }

    }

    public static class Items {
        public static final TagKey<Item> SHARDS = createTag("shards");
        public static final TagKey<Item> KNAPPED_STONE = createTag("knapped_stone");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ProgressionMod.of(name));
        }
    }
}
