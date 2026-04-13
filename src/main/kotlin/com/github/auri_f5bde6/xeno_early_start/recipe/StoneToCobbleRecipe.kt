package com.github.auri_f5bde6.xeno_early_start.recipe

import com.github.auri_f5bde6.xeno_early_start.loot.conditions.MatchToolTier
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartRecipeRegistry
import com.github.auri_f5bde6.xeno_early_start.utils.BlockList
import com.github.auri_f5bde6.xeno_early_start.utils.CodecRecipeSerializer
import com.github.auri_f5bde6.xeno_early_start.utils.PacketUtils
import com.github.auri_f5bde6.xeno_early_start.utils.Partial
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.loot.context.LootContextParameterSet
import net.minecraft.loot.context.LootContextParameters
import net.minecraft.loot.context.LootContextTypes
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.registry.Registries
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class StoneToCobbleRecipe(
    var recipeId: Identifier,
    var minedBlock: BlockList,
    var resultingBlock: Block,
    var toolTierCondition: MatchToolTier?,
    var lootTable: Identifier?,
) : Recipe<SimpleInventory> {
    fun matches(
        state: BlockState,
        itemStack: ItemStack
    ): Boolean {
        return minedBlock.test(state) && toolTierCondition?.test(itemStack) ?: true
    }

    fun rollLootTable(
        world: ServerWorld,
        pos: BlockPos,
        blockEntity: BlockEntity?,
        state: BlockState,
        itemStack: ItemStack
    ): List<ItemStack> {
        return if (lootTable != null) {
            val params = LootContextParameterSet.Builder(world)
                .add(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos))
                .add(LootContextParameters.TOOL, itemStack)
                .addOptional(LootContextParameters.BLOCK_ENTITY, blockEntity)
                .add(LootContextParameters.BLOCK_STATE, state)
                .build(LootContextTypes.BLOCK)
            world.server.lootManager.getLootTable(lootTable).generateLoot(params)
        } else {
            listOf()
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

    class Incomplete(
        val minedBlock: BlockList,
        val resultingBlock: Block,
        var toolTierCondition: MatchToolTier?,
        var lootTable: Identifier?,
    ) : Partial<StoneToCobbleRecipe> {
        override fun withId(id: Identifier): StoneToCobbleRecipe {
            return StoneToCobbleRecipe(
                id,
                minedBlock,
                resultingBlock,
                toolTierCondition,
                lootTable
            )
        }
    }

    companion object {
        val CODEC: MapCodec<Incomplete> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                BlockList.CODEC.fieldOf("mined_block").forGetter(Incomplete::minedBlock),
                Registries.BLOCK.codec.fieldOf("resulting_block").forGetter(Incomplete::resultingBlock),
                MatchToolTier.CODEC.codec().optionalFieldOf("tool_tier", null).forGetter(Incomplete::toolTierCondition),
                Identifier.CODEC.optionalFieldOf("loot_table", null).forGetter(Incomplete::lootTable)
            ).apply(instance, ::Incomplete)
        }
    }

    class Serializer : CodecRecipeSerializer<StoneToCobbleRecipe, Incomplete>(CODEC.codec()) {
        override fun read(
            id: Identifier,
            buf: PacketByteBuf
        ): StoneToCobbleRecipe {
            val minedBlock = BlockList.fromBuf(buf)
            val resultingBlock = PacketUtils.readBlock(buf)
            val hasToolTierCondition = buf.readBoolean()
            var toolTierCondition: MatchToolTier? = null
            if (hasToolTierCondition) {
                toolTierCondition = MatchToolTier.fromBuf(buf)
            }
            val hasLootTable = buf.readBoolean()
            var lootTable: Identifier? = null
            if (hasLootTable) {
                lootTable = buf.readIdentifier()
            }
            return StoneToCobbleRecipe(id, minedBlock, resultingBlock, toolTierCondition, lootTable)
        }

        override fun write(
            buf: PacketByteBuf,
            recipe: StoneToCobbleRecipe
        ) {
            recipe.minedBlock.toBuf(buf)
            PacketUtils.writeBlock(buf, recipe.resultingBlock)
            buf.writeBoolean(recipe.toolTierCondition != null)
            if (recipe.toolTierCondition != null) {
                recipe.toolTierCondition!!.toBuf(buf)
            }
            buf.writeBoolean(recipe.lootTable != null)
            if (recipe.lootTable != null) {
                buf.writeIdentifier(recipe.lootTable!!)
            }
        }

    }
}
