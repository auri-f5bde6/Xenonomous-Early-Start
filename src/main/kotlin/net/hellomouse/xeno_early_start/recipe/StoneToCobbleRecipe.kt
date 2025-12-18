package net.hellomouse.xeno_early_start.recipe

import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import net.hellomouse.xeno_early_start.ProgressionModConfig
import net.hellomouse.xeno_early_start.registries.ProgressionModRecipeRegistry
import net.hellomouse.xeno_early_start.utils.JsonUtils.getBool
import net.hellomouse.xeno_early_start.utils.JsonUtils.getFloat
import net.hellomouse.xeno_early_start.utils.JsonUtils.getItem
import net.hellomouse.xeno_early_start.utils.JsonUtils.getString
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
            )) && (!this.isOreToStone || (ProgressionModConfig.config.oreChanges.oreToStone))
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
            if (ProgressionModConfig.config.earlyGameChanges.overridePebbleDropProbability && i.isPebble) {
                probability = ProgressionModConfig.config.earlyGameChanges.pebbleDropProbability / 100f
            } else if (ProgressionModConfig.config.earlyGameChanges.overridePlantFiberProbability && i.isPlantFiber) {
                probability = ProgressionModConfig.config.earlyGameChanges.plantFiberDropProbability / 100f
            }
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
        return ProgressionModRecipeRegistry.BLOCK_TO_BLOCK.get()
    }

    override fun getType(): RecipeType<*> {
        return ProgressionModRecipeRegistry.BLOCK_TO_BLOCK_TYPE.get()
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
        var probability: Float,
        var isAffectedByFortune: Boolean,
        var isPebble: Boolean,
        var isPlantFiber: Boolean
    ) {
        fun write(buf: PacketByteBuf) {
            buf.writeIdentifier(ForgeRegistries.ITEMS.getKey(item))
            buf.writeFloat(this.probability)
            buf.writeBoolean(this.isAffectedByFortune)
            buf.writeBoolean(this.isPebble)
            buf.writeBoolean(this.isPlantFiber)
        }

        companion object {
            fun read(buf: PacketByteBuf): DroppedItem {
                val item = ForgeRegistries.ITEMS.getValue(buf.readIdentifier())!!
                val probability = buf.readFloat()
                val affectedByFortune = buf.readBoolean()
                val pebble = buf.readBoolean()
                val plantFiber = buf.readBoolean()
                return DroppedItem(item, probability, affectedByFortune, pebble, plantFiber)
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
                val plantFiber = getBool(obj, "plant_fiber") ?: false
                return DroppedItem(item, probability, affectedByFortune, pebble, plantFiber)
            }
        }
    }

}
