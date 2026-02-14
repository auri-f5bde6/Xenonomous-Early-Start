package com.github.auri_f5bde6.xeno_early_start.recipe

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStartConfig
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartRecipeRegistry
import com.github.auri_f5bde6.xeno_early_start.utils.JsonUtils.getBool
import com.github.auri_f5bde6.xeno_early_start.utils.JsonUtils.getFloat
import com.github.auri_f5bde6.xeno_early_start.utils.JsonUtils.getItem
import com.github.auri_f5bde6.xeno_early_start.utils.JsonUtils.getString
import com.github.auri_f5bde6.xeno_early_start.utils.MiningLevel
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
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
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.TierSortingRegistry
import net.minecraftforge.registries.ForgeRegistries

class StoneToCobbleRecipe(
    var recipeId: Identifier,
    var minedBlock: Identifier,
    var resultingBlock: Block,
    var droppedItems: Array<DroppedItem>,
    var miningTierLowerThan: Identifier?,
    var isDropBlockLootTable: Boolean,
    var isOreToStone: Boolean,
    var minedBlockIsTag: Boolean,
    var isAnyTier: Boolean,
    var matchHeldItems: Array<Identifier>,
    var matchHeldItemsIsTag: Array<Boolean>
) : Recipe<SimpleInventory> {
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
        if ((this.isAnyTier || MiningLevel.isToolLowerThanTier(
                itemStack, this.getMiningTierLowerThan()
            )) && (!this.isOreToStone || (XenoEarlyStartConfig.config.oreChanges.oreToStone))
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
            val probability = i.probability
            if (level.random.nextFloat() < probability) {
                Block.dropStack(level, pos, i.item.defaultStack)
            }
        }
    }


    override fun getOutput(registryManager: DynamicRegistryManager?): ItemStack? {
        return resultingBlock.asItem().defaultStack
    }

    override fun getId(): Identifier {
        return recipeId
    }


    override fun getSerializer(): RecipeSerializer<*> {
        return XenoEarlyStartRecipeRegistry.BLOCK_TO_BLOCK.get()
    }

    override fun getType(): RecipeType<*> {
        return XenoEarlyStartRecipeRegistry.BLOCK_TO_BLOCK_TYPE.get()
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
        var item: Item,
        var normalProbability: Float,
        var isAffectedByFortune: Boolean,
        var isPebble: Boolean,
    ) {
        val probability: Float
            get() = if (XenoEarlyStartConfig.config.earlyGameChanges.overridePebbleDropProbability && isPebble) {
                XenoEarlyStartConfig.config.earlyGameChanges.pebbleDropProbability
            } else {
                normalProbability
            }

        fun write(buf: PacketByteBuf) {
            buf.writeIdentifier(ForgeRegistries.ITEMS.getKey(item))
            buf.writeFloat(this.normalProbability)
            buf.writeBoolean(this.isAffectedByFortune)
            buf.writeBoolean(this.isPebble)
        }

        companion object {
            fun read(buf: PacketByteBuf): DroppedItem {
                val item = ForgeRegistries.ITEMS.getValue(buf.readIdentifier())!!
                val probability = buf.readFloat()
                val affectedByFortune = buf.readBoolean()
                val pebble = buf.readBoolean()
                return DroppedItem(item, probability, affectedByFortune, pebble)
            }

            fun fromJson(element: JsonElement): DroppedItem {
                val obj = element.asJsonObject
                val item = getItem(obj, "item") ?: throw JsonSyntaxException(
                    "Expected item to be an valid item identifier, got ${
                        getString(
                            obj,
                            "item"
                        )
                    }"
                )
                val probability = getFloat(obj, "probability") ?: 1.0f
                val affectedByFortune = getBool(obj, "affected_by_fortune") ?: false
                val pebble = getBool(obj, "pebble") ?: false
                return DroppedItem(item, probability, affectedByFortune, pebble)
            }
        }
    }

}
