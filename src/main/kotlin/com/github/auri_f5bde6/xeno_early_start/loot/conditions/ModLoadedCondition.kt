package com.github.auri_f5bde6.xeno_early_start.loot.conditions

import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartLootTypeRegistry
import com.github.auri_f5bde6.xeno_early_start.utils.CodecJsonSerializer
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.condition.LootConditionType
import net.minecraft.loot.context.LootContext
import net.minecraft.util.JsonSerializer
import net.minecraftforge.fml.ModList

class ModLoadedCondition(val modid: String) : LootCondition {
    companion object {
        val CODEC: MapCodec<ModLoadedCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.fieldOf("modid").forGetter(
                    ModLoadedCondition::modid
                )
            ).apply(instance, ::ModLoadedCondition)
        }
    }

    override fun getType(): LootConditionType {
        return XenoEarlyStartLootTypeRegistry.modLoadedCondition.get()
    }

    override fun test(t: LootContext): Boolean {
        return ModList.get().isLoaded(modid)
    }

    class Serializer : JsonSerializer<ModLoadedCondition> by CodecJsonSerializer(CODEC.codec())
}