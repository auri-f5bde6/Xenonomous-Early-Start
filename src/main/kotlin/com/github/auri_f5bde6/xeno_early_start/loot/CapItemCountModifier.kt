package com.github.auri_f5bde6.xeno_early_start.loot

import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartCodecRegistry
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.context.LootContext
import net.minecraft.util.Identifier
import net.minecraftforge.common.loot.IGlobalLootModifier
import net.minecraftforge.registries.ForgeRegistries
import java.util.function.Function
import kotlin.math.min

class CapItemCountModifier(conditionsIn: Array<LootCondition>, val item: Item, val max: Int) :
    net.minecraftforge.common.loot.LootModifier(conditionsIn) {

    @Suppress("deprecation")
    override fun doApply(
        generatedLoot: ObjectArrayList<ItemStack?>,
        context: LootContext
    ): ObjectArrayList<ItemStack?> {

        for (i in 0..<generatedLoot.count()) {
            val loot = generatedLoot[i]
            if (loot != null && loot.isOf(item)) {
                loot.count = min(loot.count, max)
            }
        }
        return generatedLoot
    }

    override fun codec(): Codec<out IGlobalLootModifier> {
        return XenoEarlyStartCodecRegistry.CAP_ITEM_COUNT_MODIFIER_TYPE.get()
    }

    companion object {
        val CODEC: MapCodec<CapItemCountModifier> =
            RecordCodecBuilder.mapCodec(Function { instance: RecordCodecBuilder.Instance<CapItemCountModifier> ->
                instance.group(
                    LOOT_CONDITIONS_CODEC.fieldOf("conditions")
                        .forGetter { glm: CapItemCountModifier -> glm.conditions },
                    Identifier.CODEC.fieldOf("item")
                        .forGetter { obj: CapItemCountModifier -> ForgeRegistries.ITEMS.getKey(obj.item)!! },
                    Codec.INT.fieldOf("max").forGetter { obj: CapItemCountModifier -> obj.max },
                ).apply(
                    instance,
                    ::fromIdentifier
                )
            })

        fun fromIdentifier(conditionsIn: Array<LootCondition>, item: Identifier, max: Int): CapItemCountModifier {
            val matchItem = ForgeRegistries.ITEMS.getValue(item)
            if (matchItem == null || matchItem == Items.AIR) {
                throw IllegalArgumentException("$item is not a valid identifier for item")
            }
            return CapItemCountModifier(conditionsIn, matchItem, max)
        }
    }
}