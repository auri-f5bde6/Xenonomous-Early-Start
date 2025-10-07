package net.hellomouse.xeno_early_start.registries

import net.hellomouse.xeno_early_start.ProgressionMod
import net.hellomouse.xeno_early_start.loot.ConfigLootFunction
import net.hellomouse.xeno_early_start.loot.PickaxeTier
import net.minecraft.loot.condition.LootConditionType
import net.minecraft.loot.function.LootFunctionType
import net.minecraft.registry.Registries
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier

object ProgressionModLootTypeRegistry {
    val COND_DEF_REG: DeferredRegister<LootConditionType> = DeferredRegister.create(
        Registries.LOOT_CONDITION_TYPE.getKey(),
        ProgressionMod.Companion.MODID
    )

    @JvmField
    val pickaxeTier: RegistryObject<LootConditionType> = COND_DEF_REG.register(
        "pickaxe_tier",
        Supplier { LootConditionType(PickaxeTier.Serializer()) })

    @JvmField
    val FUNC_DEF_REG: DeferredRegister<LootFunctionType> = DeferredRegister.create(
        Registries.LOOT_FUNCTION_TYPE.getKey(),
        ProgressionMod.Companion.MODID
    )

    @JvmField
    val configLootFunction: RegistryObject<LootFunctionType> = FUNC_DEF_REG.register(
        "config",
        Supplier { LootFunctionType(ConfigLootFunction.Serializer()) })
}
