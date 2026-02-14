package com.github.auri_f5bde6.xeno_early_start.loot

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartCodecRegistry
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.item.ItemStack
import net.minecraft.loot.LootDataType
import net.minecraft.loot.LootTable
import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.context.LootContext
import net.minecraft.util.Identifier
import net.minecraftforge.common.loot.IGlobalLootModifier
import net.minecraftforge.common.loot.LootModifier
import java.util.function.Function

class ReplaceTableLootModifier(conditionsIn: Array<LootCondition>, private val table: Identifier) :
    LootModifier(conditionsIn) {

    fun table(): Identifier {
        return this.table
    }

    @Suppress("deprecation")
    override fun doApply(
        generatedLoot: ObjectArrayList<ItemStack?>,
        context: LootContext
    ): ObjectArrayList<ItemStack?> {
        val newLoot = ObjectArrayList.of<ItemStack?>()
        val a = context.dataLookup.getElement(LootDataType.LOOT_TABLES, this.table)
        if (a != null) {
            a.generateUnprocessedLoot(context, LootTable.processStacks(context.world, newLoot::add))
        } else {
            XenoEarlyStart.LOGGER.error("No table called ${this.table} is found for ReplaceLootTableModifier")
        }
        return newLoot
    }

    override fun codec(): Codec<out IGlobalLootModifier> {
        return XenoEarlyStartCodecRegistry.REPLACE_TABLE_LOOT_MODIFIER_TYPE.get()
    }

    companion object {
        val CODEC: MapCodec<ReplaceTableLootModifier> =
            RecordCodecBuilder.mapCodec(Function { instance: RecordCodecBuilder.Instance<ReplaceTableLootModifier> ->
                instance.group(
                    LOOT_CONDITIONS_CODEC.fieldOf("conditions")
                        .forGetter { glm: ReplaceTableLootModifier -> glm.conditions },
                    Identifier.CODEC.fieldOf("table")
                        .forGetter { obj: ReplaceTableLootModifier -> obj.table() }
                ).apply(
                    instance,
                    ::ReplaceTableLootModifier
                )
            })
    }
}