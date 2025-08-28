package net.hellomouse.xeno_early_start

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey

class ProgressionModTags {
    object Blocks {
        val HAS_BLOCK_TO_BLOCK_RECIPE: TagKey<Block> = createTag("has_block_to_block_recipe")

        private fun createTag(name: String): TagKey<Block> {
            return BlockTags.create(ProgressionMod.Companion.of(name))
        }
    }

    object Items {
        val SHARDS: TagKey<Item> = createTag("shards")
        val KNAPPED_STONE: TagKey<Item> = createTag("knapped_stone")

        private fun createTag(name: String): TagKey<Item> {
            return ItemTags.create(ProgressionMod.Companion.of(name))
        }
    }
}
