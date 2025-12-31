package com.github.auri_f5bde6.xeno_early_start.registries

import com.github.auri_f5bde6.xeno_early_start.MultiplicativeSpawnModifier
import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.loot.AddTableLootModifier
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryCodecs
import net.minecraftforge.common.loot.IGlobalLootModifier
import net.minecraftforge.common.world.BiomeModifier
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import java.util.function.Supplier


object XenoEarlyStartCodecRegistry {
    var BIOME_MODIFIER_DEF_REG: DeferredRegister<Codec<out BiomeModifier?>?> =
        DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, XenoEarlyStart.MODID)
    var MULTIPLICATIVE_SPAWN_MODIFIER: Supplier<Codec<MultiplicativeSpawnModifier>> =
        BIOME_MODIFIER_DEF_REG.register<Codec<MultiplicativeSpawnModifier>>("multiplicative_spawn_modifier") {
            RecordCodecBuilder.create { builder ->
                builder.group(
                    Codec.STRING.fieldOf("spawn_group").forGetter { it.spawnGroup },
                    RegistryCodecs.entryList(Registries.ENTITY_TYPE.key).fieldOf("entity_types")
                        .forGetter { it.entityTypeIds },
                    Codec.FLOAT.fieldOf("multiplier").forGetter { it.multiplier }
                ).apply(builder, ::MultiplicativeSpawnModifier)
            }
        }
    val GLOBAL_LOOT_MODIFIER_DEF_REG: DeferredRegister<Codec<out IGlobalLootModifier>> =
        DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, XenoEarlyStart.MODID)
    var ADD_TABLE_LOOT_MODIFIER_TYPE: Supplier<Codec<AddTableLootModifier>> =
        GLOBAL_LOOT_MODIFIER_DEF_REG.register("add_table") { AddTableLootModifier.CODEC.codec() }
}