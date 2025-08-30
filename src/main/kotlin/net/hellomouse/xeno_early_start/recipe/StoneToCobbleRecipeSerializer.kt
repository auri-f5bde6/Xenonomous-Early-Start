package net.hellomouse.xeno_early_start.recipe

import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import net.hellomouse.xeno_early_start.utils.JsonUtils
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper
import net.minecraftforge.registries.ForgeRegistries

class Serializer : RecipeSerializer<StoneToCobbleRecipe> {
    override fun read(id: Identifier, json: JsonObject): StoneToCobbleRecipe {
        var minedBlockIsTag = false
        var minedBlockStr = json.get("mined_block").asString
        if (minedBlockStr[0] == '#') {
            minedBlockIsTag = true
            minedBlockStr = minedBlockStr.substring(1)
        }
        val minedBlock = Identifier.parse(minedBlockStr)
        val resultingBlock = ForgeRegistries.BLOCKS.getValue(Identifier.parse(json.get("resulting_block").asString))
            ?: throw JsonSyntaxException(
                "Expected resulting_block to be a valid block identifier, got ${
                    JsonUtils.getString(
                        json, "resulting_block"
                    )
                }"
            )
        val droppedItems = JsonUtils.getArray<StoneToCobbleRecipe.DroppedItem>(
            json, "dropped_items", StoneToCobbleRecipe.DroppedItem.Companion::fromJson
        ) ?: arrayOf()
        val miningTierLowerThan =
            JsonUtils.getIdentifier(json, "mining_tier_lower_than") ?: Identifier.of("minecraft", "wood")
        val dropBlockLootTable = JsonUtils.getBool(json, "drop_block_loot_table") ?: false
        val isOreToStone = JsonUtils.getBool(json, "ore_to_stone") ?: false
        val anyTier = JsonUtils.getBool(json, "any_tier") ?: false
        var matchHeldItems: Array<Identifier?>? = null
        var matchHeldItemsIsTag: Array<Boolean?>? = null
        val value = json.get("held_item_match_any")
        if (value != null) {
            if (value.isJsonArray) {
                for ((i, v) in value.asJsonArray.withIndex()) {
                    val size = value.asJsonArray.size()
                    matchHeldItems = arrayOfNulls(size)
                    matchHeldItemsIsTag = arrayOfNulls(size)
                    val itemOrTag: String = v.asString
                    if (itemOrTag[0] == '#') {
                        matchHeldItems[i] = Identifier.parse(itemOrTag.substring(1))
                        matchHeldItemsIsTag[i] = true
                    } else {
                        matchHeldItems[i] = Identifier.parse(itemOrTag)
                        matchHeldItemsIsTag[i] = false
                    }
                }
            } else {
                throw JsonSyntaxException(
                    "Expected held_item_match_any to be a JsonArray, was " + JsonHelper.getType(
                        value
                    )
                )
            }
        }
        @Suppress("UNCHECKED_CAST") // It's fine as each null should be replaced in the loop
        return StoneToCobbleRecipe(
            id,
            minedBlock,
            resultingBlock,
            droppedItems,
            miningTierLowerThan,
            dropBlockLootTable,
            isOreToStone,
            minedBlockIsTag,
            anyTier,
            (matchHeldItems ?: arrayOf()) as Array<Identifier>,
            (matchHeldItemsIsTag ?: arrayOf()) as Array<Boolean>
        )
    }

    override fun read(id: Identifier, buf: PacketByteBuf): StoneToCobbleRecipe {
        val minedBlock = buf.readIdentifier()
        val resultingBlock = ForgeRegistries.BLOCKS.getValue(buf.readIdentifier())!!
        val droppedItemSize = buf.readInt()
        val droppedItems = arrayOfNulls<StoneToCobbleRecipe.DroppedItem>(droppedItemSize)
        for (i in 0..<droppedItemSize) {
            droppedItems[i] = StoneToCobbleRecipe.DroppedItem.Companion.read(buf)
        }
        val miningTierLowerThan = buf.readIdentifier()
        val dropBlockLootTable = buf.readBoolean()
        val isOreToStone = buf.readBoolean()
        val minedBlockIsTag = buf.readBoolean()
        val anyTier = buf.readBoolean()
        val matchHeldItemSize = buf.readInt()
        val matchHeldItem = arrayOfNulls<Identifier?>(matchHeldItemSize)
        val matchHeldItemIsTag = arrayOfNulls<Boolean?>(matchHeldItemSize)
        for (i in 0..<matchHeldItemSize) {
            matchHeldItem[i] = buf.readIdentifier()
            matchHeldItemIsTag[i] = buf.readBoolean()
        }
        @Suppress("UNCHECKED_CAST") // It's fine as each null should be replaced in the loop
        return StoneToCobbleRecipe(
            id,
            minedBlock,
            resultingBlock,
            droppedItems as Array<StoneToCobbleRecipe.DroppedItem>,
            miningTierLowerThan,
            dropBlockLootTable,
            isOreToStone,
            minedBlockIsTag,
            anyTier,
            matchHeldItem as Array<Identifier>,
            matchHeldItemIsTag as Array<Boolean>
        )
    }

    override fun write(buf: PacketByteBuf, recipe: StoneToCobbleRecipe) {
        buf.writeIdentifier(recipe.minedBlock)
        buf.writeIdentifier(ForgeRegistries.BLOCKS.getKey(recipe.resultingBlock))
        buf.writeInt(recipe.droppedItems.size)
        for (i in recipe.droppedItems) {
            i.write(buf)
        }
        buf.writeIdentifier(recipe.miningTierLowerThan)
        buf.writeBoolean(recipe.isDropBlockLootTable)
        buf.writeBoolean(recipe.isOreToStone)
        buf.writeBoolean(recipe.minedBlockIsTag)
        buf.writeBoolean(recipe.isAnyTier)
        buf.writeInt(recipe.matchHeldItems.size)
        for (i in recipe.matchHeldItemsIsTag.indices) {
            buf.writeIdentifier(recipe.matchHeldItems[i])
            buf.writeBoolean(recipe.matchHeldItemsIsTag[i])
        }
    }
}