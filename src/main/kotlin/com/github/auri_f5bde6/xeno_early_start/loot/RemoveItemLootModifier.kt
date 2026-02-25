package com.github.auri_f5bde6.xeno_early_start.loot

import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartCodecRegistry
import com.mojang.datafixers.util.Either
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
    private val remove: Either<Item, TagKey<Item>>,
    private val chance: Float,
) :
    LootModifier(conditionsIn) {

    @Suppress("deprecation")
    override fun doApply(
        generatedLoot: ObjectArrayList<ItemStack?>,
        context: LootContext
    ): ObjectArrayList<ItemStack?> {
        if (chance >= 1f || context.random.nextFloat() < chance) {
            return ObjectArrayList<ItemStack?>(generatedLoot.stream().filter { itemStack ->
                if (itemStack == null) {
                    false
                } else {
                    !remove.map({ i -> itemStack.isOf(i) }, { tag -> itemStack.isIn(tag) })
                }
            }.iterator())
        }
        return generatedLoot
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
                    Codec.either(
                        Registries.ITEM.codec,
                        TagKey.codec(Registries.ITEM.key)
                    ).fieldOf("remove")
                        .forGetter { obj: RemoveItemLootModifier -> obj.remove },
                    Codec.FLOAT.fieldOf("chance")
                        .orElse(1.0f)
                        .forGetter { obj: RemoveItemLootModifier -> obj.chance }
                ).apply(
                    instance,
                    ::RemoveItemLootModifier
                )
            })
    }
}