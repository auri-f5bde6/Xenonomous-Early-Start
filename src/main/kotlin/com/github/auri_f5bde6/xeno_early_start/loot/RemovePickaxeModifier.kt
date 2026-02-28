package com.github.auri_f5bde6.xeno_early_start.loot

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStartConfig
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartCodecRegistry
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.item.ItemStack
import net.minecraft.item.PickaxeItem
import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.context.LootContext
import net.minecraftforge.common.loot.IGlobalLootModifier
import net.minecraftforge.common.loot.LootModifier
import java.util.function.Function

class RemovePickaxeModifier(
    conditionsIn: Array<LootCondition>
) :
    LootModifier(conditionsIn) {

    override fun doApply(
        generatedLoot: ObjectArrayList<ItemStack?>,
        context: LootContext
    ): ObjectArrayList<ItemStack?> {
        return if (XenoEarlyStartConfig.config.earlyGameChanges.removePickaxeFromAllLootTable) {
            ObjectArrayList<ItemStack?>(generatedLoot.stream().filter { itemStack ->
                if (itemStack == null) {
                    false
                } else {
                    itemStack.item !is PickaxeItem
                }
            }.iterator())
        } else {
            generatedLoot
        }
    }

    override fun codec(): Codec<out IGlobalLootModifier> {
        return XenoEarlyStartCodecRegistry.REMOVE_PICKAXE_LOOT_MODIFIER_TYPE.get()
    }

    companion object {
        val CODEC: MapCodec<RemovePickaxeModifier> =
            RecordCodecBuilder.mapCodec(Function { instance: RecordCodecBuilder.Instance<RemovePickaxeModifier> ->
                instance.group(
                    LOOT_CONDITIONS_CODEC.fieldOf("conditions")
                        .forGetter { glm: RemovePickaxeModifier -> glm.conditions }
                ).apply(
                    instance,
                    ::RemovePickaxeModifier
                )
            })
    }
}