package net.hellomouse.progression_change.registries;

import net.hellomouse.progression_change.ProgressionMod;
import net.hellomouse.progression_change.recipe.StoneToCobbleRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.RegistryKeys;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ProgressionModRecipeRegistry {
    public static final DeferredRegister<RecipeType<?>> TYPE_DEF_REG = DeferredRegister.create(RegistryKeys.RECIPE_TYPE, ProgressionMod.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> DEF_REG = DeferredRegister.create(RegistryKeys.RECIPE_SERIALIZER, ProgressionMod.MODID);
    public static final RegistryObject<RecipeType<StoneToCobbleRecipe>> BLOCK_TO_BLOCK_TYPE = TYPE_DEF_REG.register("block_to_block", () -> new RecipeType<>() {
    });
    public static final RegistryObject<RecipeSerializer<StoneToCobbleRecipe>> BLOCK_TO_BLOCK = DEF_REG.register("block_to_block", StoneToCobbleRecipe.Serializer::new);
}
