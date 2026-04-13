package com.github.auri_f5bde6.xeno_early_start.registries

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.biome.ConfigBiomeModifier
import com.github.auri_f5bde6.xeno_early_start.biome.ConfigBiomeModifier.Type
import com.github.auri_f5bde6.xeno_early_start.biome.MultiplicativeSpawnModifier
import com.github.auri_f5bde6.xeno_early_start.loot.modifiers.*
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
    val BIOME_MODIFIER_DEF_REG: DeferredRegister<Codec<out BiomeModifier?>?> =
        DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, XenoEarlyStart.MODID)
    val MULTIPLICATIVE_SPAWN_MODIFIER: Supplier<Codec<MultiplicativeSpawnModifier>> =
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
    val CONFIG_BIOME_MODIFIER: Supplier<Codec<ConfigBiomeModifier>> = BIOME_MODIFIER_DEF_REG.register("config") {
        RecordCodecBuilder.create { instance ->
            instance.group(
                Type.CODEC.fieldOf("config").forGetter(ConfigBiomeModifier::config),
                BiomeModifier.DIRECT_CODEC.fieldOf("biomeModifier").forGetter(ConfigBiomeModifier::biomeModifier)
            ).apply(instance, ::ConfigBiomeModifier)
        }
    }
    val GLOBAL_LOOT_MODIFIER_DEF_REG: DeferredRegister<Codec<out IGlobalLootModifier>> =
        DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, XenoEarlyStart.MODID)
    val ADD_TABLE_LOOT_MODIFIER_TYPE: Supplier<Codec<AddTableLootModifier>> =
        GLOBAL_LOOT_MODIFIER_DEF_REG.register("add_table") { AddTableLootModifier.CODEC.codec() }
    val REPLACE_TABLE_LOOT_MODIFIER_TYPE: Supplier<Codec<ReplaceTableLootModifier>> =
        GLOBAL_LOOT_MODIFIER_DEF_REG.register("replace_table") { ReplaceTableLootModifier.CODEC.codec() }
    val CAP_ITEM_COUNT_MODIFIER_TYPE: Supplier<Codec<CapItemCountModifier>> =
        GLOBAL_LOOT_MODIFIER_DEF_REG.register("cap_item_count") { CapItemCountModifier.CODEC.codec() }
    val REMOVE_ITEM_LOOT_MODIFIER_TYPE: Supplier<Codec<RemoveItemLootModifier>> =
        GLOBAL_LOOT_MODIFIER_DEF_REG.register("remove_items") { RemoveItemLootModifier.CODEC.codec() }
    val REMOVE_TOOLS_LOOT_MODIFIER_TYPE: Supplier<Codec<RemoveToolModifier>> =
        GLOBAL_LOOT_MODIFIER_DEF_REG.register("remove_tools") { RemoveToolModifier.CODEC.codec() }
}