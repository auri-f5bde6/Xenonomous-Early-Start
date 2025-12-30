package net.hellomouse.xeno_early_start.loot

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.hellomouse.xeno_early_start.registries.XenoEarlyStartCodecRegistry
import net.minecraft.item.ItemStack
import net.minecraft.loot.LootDataType
import net.minecraft.loot.LootTable
import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.context.LootContext
import net.minecraft.util.Identifier
import net.minecraftforge.common.loot.IGlobalLootModifier
import net.minecraftforge.common.loot.LootModifier
import java.util.function.Function

// https://github.com/neoforged/NeoForge/blob/1.21.x/src/main/java/net/neoforged/neoforge/common/loot/AddTableLootModifier.java#
class AddTableLootModifier(conditionsIn: Array<LootCondition>, private val table: Identifier) :
    LootModifier(conditionsIn) {

    fun table(): Identifier {
        return this.table
    }

    @Suppress("deprecation")
    override fun doApply(
        generatedLoot: ObjectArrayList<ItemStack?>,
        context: LootContext
    ): ObjectArrayList<ItemStack?> {
        val a = context.dataLookup.getElement(LootDataType.LOOT_TABLES, this.table)
        a?.generateUnprocessedLoot(context, LootTable.processStacks(context.world, generatedLoot::add))
        return generatedLoot
    }

    override fun codec(): Codec<out IGlobalLootModifier> {
        return XenoEarlyStartCodecRegistry.ADD_TABLE_LOOT_MODIFIER_TYPE.get()
    }

    companion object {
        val CODEC: MapCodec<AddTableLootModifier> =
            RecordCodecBuilder.mapCodec<AddTableLootModifier>(Function { instance: RecordCodecBuilder.Instance<AddTableLootModifier> ->
                instance.group(
                    LOOT_CONDITIONS_CODEC.fieldOf("conditions")
                        .forGetter { glm: AddTableLootModifier -> glm.conditions },
                    Identifier.CODEC.fieldOf("table")
                        .forGetter { obj: AddTableLootModifier -> obj.table() }
                ).apply(
                    instance,
                    ::AddTableLootModifier
                )
            })
    }
}