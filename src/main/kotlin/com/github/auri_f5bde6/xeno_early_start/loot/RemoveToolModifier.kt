package com.github.auri_f5bde6.xeno_early_start.loot

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStartConfig
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartCodecRegistry
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.item.AxeItem
import net.minecraft.item.ItemStack
import net.minecraft.item.PickaxeItem
import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.context.LootContext
import net.minecraftforge.common.loot.IGlobalLootModifier
import net.minecraftforge.common.loot.LootModifier
import java.util.function.Function

class RemoveToolModifier(
    conditionsIn: Array<LootCondition>,
    val type: Type
) :
    LootModifier(conditionsIn) {
    enum class Type(val literal: String) {
        Pickaxe("pickaxe"),
        Axe("axe");

        companion object {
            fun fromString(type: String) =
                entries.find { v -> v.literal == type }

            val CODEC: Codec<Type> = Codec.STRING.comapFlatMap({ s ->
                val t = fromString(s)
                if (t != null) {
                    return@comapFlatMap DataResult.success(t)
                } else {
                    return@comapFlatMap DataResult.error { "$s is not a valid type" }
                }
            }, Type::literal)
        }
    }

    override fun doApply(
        generatedLoot: ObjectArrayList<ItemStack?>,
        context: LootContext
    ): ObjectArrayList<ItemStack?> {
        return if (XenoEarlyStartConfig.config.earlyGameChanges.removePickaxeFromAllLootTable) {
            ObjectArrayList<ItemStack?>(generatedLoot.stream().filter { itemStack ->
                if (itemStack == null) {
                    false
                } else {
                    !((itemStack.item is PickaxeItem && type == Type.Pickaxe) || (itemStack.item is AxeItem && type == Type.Axe))
                }
            }.iterator())
        } else {
            generatedLoot
        }
    }

    override fun codec(): Codec<out IGlobalLootModifier> {
        return XenoEarlyStartCodecRegistry.REMOVE_TOOLS_LOOT_MODIFIER_TYPE.get()
    }

    companion object {
        val CODEC: MapCodec<RemoveToolModifier> =
            RecordCodecBuilder.mapCodec(Function { instance: RecordCodecBuilder.Instance<RemoveToolModifier> ->
                instance.group(
                    LOOT_CONDITIONS_CODEC.fieldOf("conditions")
                        .forGetter { glm: RemoveToolModifier -> glm.conditions },
                    Type.CODEC.fieldOf("tool").forGetter { glm -> glm.type },
                ).apply(
                    instance,
                    ::RemoveToolModifier
                )
            })
    }
}