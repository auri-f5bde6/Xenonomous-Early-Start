package com.github.auri_f5bde6.xeno_early_start.loot

import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartCodecRegistry
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.context.LootContext
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.TagKey
import net.minecraftforge.common.loot.IGlobalLootModifier
import net.minecraftforge.common.loot.LootModifier
import java.util.function.Function

class RemoveItemLootModifier(
    conditionsIn: Array<LootCondition>,
    private val tag: TagKey<Item>,
) :
    LootModifier(conditionsIn) {

    @Suppress("deprecation")
    override fun doApply(
        generatedLoot: ObjectArrayList<ItemStack?>,
        context: LootContext
    ): ObjectArrayList<ItemStack?> {
        return ObjectArrayList<ItemStack?>(generatedLoot.stream().filter { itemStack ->
            return@filter !(itemStack?.isIn(tag) ?: false)
        }.iterator())
    }

    override fun codec(): Codec<out IGlobalLootModifier> {
        return XenoEarlyStartCodecRegistry.REMOVE_ITEM_LOOT_MODIFIER_TYPE.get()
    }

    companion object {
        val CODEC: MapCodec<RemoveItemLootModifier> =
            RecordCodecBuilder.mapCodec(Function { instance: RecordCodecBuilder.Instance<RemoveItemLootModifier> ->
                instance.group(
                    LOOT_CONDITIONS_CODEC.fieldOf("conditions")
                        .forGetter { glm: RemoveItemLootModifier -> glm.conditions },
                    TagKey.codec(Registries.ITEM.key).fieldOf("remove")
                        .forGetter { obj: RemoveItemLootModifier -> obj.tag }
                ).apply(
                    instance,
                    ::RemoveItemLootModifier
                )
            })
    }
}