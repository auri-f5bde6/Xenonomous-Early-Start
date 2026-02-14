package com.github.auri_f5bde6.xeno_early_start

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey

class XenoEarlyStartTags {
    object Blocks {
        val HAS_BLOCK_TO_BLOCK_RECIPE: TagKey<Block> = createTag("has_block_to_block_recipe")
        val ALWAYS_TRIGGER_EXPLOSION: TagKey<Block> = createTag("always_trigger_explosion")
        val TRIGGER_EXPLOSION_WHEN_LIT: TagKey<Block> = createTag("trigger_explosion_when_lit")
        private fun createTag(name: String): TagKey<Block> {
            return BlockTags.create(XenoEarlyStart.of(name))
        }
    }

    object Items {
        val SHARDS: TagKey<Item> = createTag("shards")
        val KNAPPED_STONE: TagKey<Item> = createTag("knapped_stone")
        val PEBBLES: TagKey<Item> = createTag("pebbles")
        val FOOD_WARNING: TagKey<Item> = createTag("food_warning")
        val RAW_FOOD_WARNING: TagKey<Item> = createTag("raw_food_warning")

        private fun createTag(name: String): TagKey<Item> {
            return ItemTags.create(XenoEarlyStart.of(name))
        }
    }
}
