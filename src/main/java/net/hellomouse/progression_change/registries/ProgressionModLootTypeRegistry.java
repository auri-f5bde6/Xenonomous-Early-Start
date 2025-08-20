package net.hellomouse.progression_change.registries;

import net.hellomouse.progression_change.ProgressionMod;
import net.hellomouse.progression_change.loot.ConfigLootFunction;
import net.hellomouse.progression_change.loot.PickaxeTier;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.Registries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ProgressionModLootTypeRegistry {
    public static final DeferredRegister<LootConditionType> COND_DEF_REG = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE.getKey(), ProgressionMod.MODID);
    public static final RegistryObject<LootConditionType> PickaxeTier = COND_DEF_REG.register("pickaxe_tier", () -> new LootConditionType(new PickaxeTier.Serializer()));
    public static final DeferredRegister<LootFunctionType> FUNC_DEF_REG = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE.getKey(), ProgressionMod.MODID);
    public static final RegistryObject<LootFunctionType> ConfigLootFunction = FUNC_DEF_REG.register("config", () -> new LootFunctionType(new ConfigLootFunction.Serializer()));
}
