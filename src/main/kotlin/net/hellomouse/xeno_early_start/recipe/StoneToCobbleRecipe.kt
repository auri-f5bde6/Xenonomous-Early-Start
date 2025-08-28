package net.hellomouse.xeno_early_start.recipe

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import net.hellomouse.xeno_early_start.ProgressionModConfig
import net.hellomouse.xeno_early_start.registries.ProgressionModRecipeRegistry
import net.hellomouse.xeno_early_start.utils.JsonUtils.getArray
import net.hellomouse.xeno_early_start.utils.JsonUtils.getBool
import net.hellomouse.xeno_early_start.utils.JsonUtils.getFloat
import net.hellomouse.xeno_early_start.utils.JsonUtils.getIdentifier
import net.hellomouse.xeno_early_start.utils.MiningLevel
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolMaterial
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.TierSortingRegistry
import net.minecraftforge.registries.ForgeRegistries

class StoneToCobbleRecipe(
    var recipeId: Identifier?,
    var minedBlock: Identifier?,
    var resultingBlock: Block,
    var droppedItems: Array<DroppedItem>,
    var miningTierLowerThan: Identifier?,
    var isDropBlockLootTable: Boolean,
    var isOreToStone: Boolean,
    var minedBlockIsTag: Boolean,
    var isAnyTier: Boolean,
    var matchHeldItems: Array<Identifier>,
    var matchHeldItemsIsTag: Array<Boolean>
) : Recipe<SimpleInventory?> {
    fun getMiningTierLowerThan(): ToolMaterial? {
        return TierSortingRegistry.byName(miningTierLowerThan)
    }

    fun matches(state: BlockState, itemStack: ItemStack): Boolean {
        for (i in matchHeldItems.indices) {
            if ((!matchHeldItemsIsTag[i] && matchHeldItems[i] === (ForgeRegistries.ITEMS.getKey(itemStack.item))) || (matchHeldItemsIsTag[i] && itemStack.isIn(
                    TagKey.of(
                        ForgeRegistries.ITEMS.getRegistryKey(), matchHeldItems[i]
                    )
                ))
            ) {
                return true
            }
        }
        if (!matchHeldItems.isEmpty()) {
            return false
        }
        if ((this.isAnyTier || MiningLevel.IsToolLowerThanTier(
                itemStack, this.getMiningTierLowerThan()
            )) && (!this.isOreToStone || (ProgressionModConfig.oreDropChanges.oreToStone))
        ) {
            if (minedBlockIsTag) {
                val tag = TagKey.of(ForgeRegistries.BLOCKS.getRegistryKey(), minedBlock)
                return state.isIn(tag)
            } else {
                return state.isOf(ForgeRegistries.BLOCKS.getValue(minedBlock))
            }
        } else {
            return false
        }
    }

    fun maybeDropItemsInList(level: World, pos: BlockPos) {
        for (i in droppedItems) {
            var probability = i.probability
            if (ProgressionModConfig.earlyGameChanges.overridePebbleDropProbability && i.isPebble) {
                probability = ProgressionModConfig.earlyGameChanges.pebbleDropProbability / 100f
            } else if (ProgressionModConfig.earlyGameChanges.overridePlantFiberProbability && i.isPlantFiber) {
                probability = ProgressionModConfig.earlyGameChanges.plantFiberDropProbability / 100f
            }
            if (level.random.nextFloat() < probability) {
                Block.dropStack(level, pos, i.getItem()!!.defaultStack)
            }
        }
    }


    override fun getOutput(registryManager: DynamicRegistryManager?): ItemStack? {
        return resultingBlock.asItem().defaultStack
    }

    override fun getId(): Identifier? {
        return recipeId
    }


    override fun getSerializer(): RecipeSerializer<*> {
        return Serializer()
    }

    override fun getType(): RecipeType<*> {
        return ProgressionModRecipeRegistry.BLOCK_TO_BLOCK_TYPE!!.get()
    }

    // These methods aren't used here but must be implemented
    override fun matches(inventory: SimpleInventory?, world: World?): Boolean {
        return false
    }

    override fun craft(inventory: SimpleInventory?, registryManager: DynamicRegistryManager?): ItemStack? {
        return null
    }

    override fun fits(width: Int, height: Int): Boolean {
        return false
    }

    class DroppedItem(
        var item: Identifier?,
        var probability: Float,
        var isAffectedByFortune: Boolean,
        var isPebble: Boolean,
        var isPlantFiber: Boolean
    ) {
        fun getItem(): Item? {
            return ForgeRegistries.ITEMS.getValue(item)
        }

        fun write(buf: PacketByteBuf) {
            buf.writeIdentifier(this.item)
            buf.writeFloat(this.probability)
            buf.writeBoolean(this.isAffectedByFortune)
            buf.writeBoolean(this.isPebble)
            buf.writeBoolean(this.isPlantFiber)
        }

        companion object {
            fun read(buf: PacketByteBuf): DroppedItem {
                val item = buf.readIdentifier()
                val probability = buf.readFloat()
                val affectedByFortune = buf.readBoolean()
                val pebble = buf.readBoolean()
                val plantFiber = buf.readBoolean()
                return DroppedItem(item, probability, affectedByFortune, pebble, plantFiber)
            }

            fun fromJson(element: JsonElement): DroppedItem {
                val obj = element.asJsonObject
                val item = Identifier.parse(obj.get("item").asString)
                val probability = getFloat(obj, "probability") ?: 1.0f
                val affectedByFortune = getBool(obj, "affected_by_fortune") ?: false
                val pebble = getBool(obj, "pebble") ?: false
                val plantFiber = getBool(obj, "plant_fiber") ?: false
                return DroppedItem(item, probability, affectedByFortune, pebble, plantFiber)
            }
        }
    }

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
                    "Expected resulting_block to be a valid block identifier, got ${json.get("resulting_block").asString}"
                )
            val droppedItems =
                getArray<DroppedItem>(json, "dropped_items", DroppedItem.Companion::fromJson) ?: arrayOf()
            val miningTierLowerThan =
                getIdentifier(json, "mining_tier_lower_than") ?: Identifier.of("minecraft", "wood")
            val dropBlockLootTable = getBool(json, "drop_block_loot_table") ?: false
            val isOreToStone = getBool(json, "ore_to_stone") ?: false
            val anyTier = getBool(json, "any_tier") ?: false
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
            val droppedItems = arrayOfNulls<DroppedItem>(droppedItemSize)
            for (i in 0..<droppedItemSize) {
                droppedItems[i] = DroppedItem.Companion.read(buf)
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
                droppedItems as Array<DroppedItem>,
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
                buf.writeBoolean(recipe.matchHeldItemsIsTag[i]!!)
            }
        }
    }
}
