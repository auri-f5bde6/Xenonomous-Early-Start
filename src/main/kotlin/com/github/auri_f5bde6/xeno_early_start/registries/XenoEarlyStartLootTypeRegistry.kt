package com.github.auri_f5bde6.xeno_early_start.registries

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.loot.BlockIsInTagCondition
import com.github.auri_f5bde6.xeno_early_start.loot.ConfigLootFunction
import com.github.auri_f5bde6.xeno_early_start.loot.LootTableIdCondition
import com.github.auri_f5bde6.xeno_early_start.loot.MatchToolTier
import net.minecraft.loot.condition.LootConditionType
import net.minecraft.loot.function.LootFunctionType
import net.minecraft.registry.Registries
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier

object XenoEarlyStartLootTypeRegistry {
    val COND_DEF_REG: DeferredRegister<LootConditionType> = DeferredRegister.create(
        Registries.LOOT_CONDITION_TYPE.getKey(),
        XenoEarlyStart.MODID
    )

    @JvmField
    val matchToolTier: RegistryObject<LootConditionType> = COND_DEF_REG.register(
        "match_tool_tier",
        Supplier { LootConditionType(MatchToolTier.Serializer()) })

    @JvmField
    val lootTableIdCondition: RegistryObject<LootConditionType> = COND_DEF_REG.register(
        "loot_table_id",
        Supplier { LootConditionType(LootTableIdCondition.Serializer()) })

    @JvmField
    val blockIsInTagCondition: RegistryObject<LootConditionType> = COND_DEF_REG.register(
        "block_is_in_tag",
        Supplier { LootConditionType(BlockIsInTagCondition.Serializer()) })

    @JvmField
    val FUNC_DEF_REG: DeferredRegister<LootFunctionType> = DeferredRegister.create(
        Registries.LOOT_FUNCTION_TYPE.getKey(),
        XenoEarlyStart.MODID
    )

    @JvmField
    val configLootFunction: RegistryObject<LootFunctionType> = FUNC_DEF_REG.register(
        "config",
        Supplier { LootFunctionType(ConfigLootFunction.Serializer()) })


}
