package com.github.auri_f5bde6.xeno_early_start

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey

class XenoEarlyStartTags {
    object Blocks {
        @JvmField
        val HAS_BLOCK_TO_BLOCK_RECIPE: TagKey<Block> = createTag("has_block_to_block_recipe")

        @JvmField
        val ALWAYS_TRIGGER_EXPLOSION: TagKey<Block> = createTag("always_trigger_explosion")

        @JvmField
        val TRIGGER_EXPLOSION_WHEN_LIT: TagKey<Block> = createTag("trigger_explosion_when_lit")

        @JvmField
        val CAN_KNAP_STONE: TagKey<Block> = createTag("can_knap_stone")

        @JvmField
        val BRICK_FURNACE: TagKey<Block> = createTag("brick_furnace")

        @JvmField
        val FURNACE: TagKey<Block> = createTag("furnace")

        private fun createTag(name: String): TagKey<Block> {
            return BlockTags.create(XenoEarlyStart.of(name))
        }
    }

    object Items {
        @JvmField
        val SHARDS: TagKey<Item> = createTag("shards")

        @JvmField

        val KNAPPED_STONE: TagKey<Item> = createTag("knapped_stone")

        @JvmField
        val PEBBLES: TagKey<Item> = createTag("pebbles")

        @JvmField
        val FOOD_WARNING: TagKey<Item> = createTag("food_warning")

        @JvmField
        val RAW_FOOD_WARNING: TagKey<Item> = createTag("raw_food_warning")

        @JvmField
        val BRICK_FURNACE_OUTPUT_BLOCKLIST: TagKey<Item> = createTag("brick_furnace_output_blocklist")

        @JvmField
        val FURNACE_OUTPUT_BLOCKLIST: TagKey<Item> = createTag("furnace_output_blocklist")

        private fun createTag(name: String): TagKey<Item> {
            return ItemTags.create(XenoEarlyStart.of(name))
        }
    }
}
