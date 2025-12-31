package com.github.auri_f5bde6.xeno_early_start.registries

import com.github.auri_f5bde6.xeno_early_start.ProgressionMod
import com.github.auri_f5bde6.xeno_early_start.recipe.PrimitiveFireRecipe
import com.github.auri_f5bde6.xeno_early_start.recipe.Serializer
import com.github.auri_f5bde6.xeno_early_start.recipe.StoneToCobbleRecipe
import net.minecraft.recipe.CookingRecipeSerializer
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.RegistryKeys
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier

object ProgressionModRecipeRegistry {
    val TYPE_DEF_REG: DeferredRegister<RecipeType<*>> =
        DeferredRegister.create(RegistryKeys.RECIPE_TYPE, ProgressionMod.MODID)
    val DEF_REG: DeferredRegister<RecipeSerializer<*>> =
        DeferredRegister.create(RegistryKeys.RECIPE_SERIALIZER, ProgressionMod.MODID)

    @JvmField
    val BLOCK_TO_BLOCK_TYPE: RegistryObject<RecipeType<StoneToCobbleRecipe>> =
        TYPE_DEF_REG.register("block_to_block", Supplier {
            object : RecipeType<StoneToCobbleRecipe> {
            }
        })

    @JvmField
    val BLOCK_TO_BLOCK: RegistryObject<RecipeSerializer<StoneToCobbleRecipe>> =
        DEF_REG.register(
            "block_to_block",
            Supplier { Serializer() })

    @JvmField
    val PRIMITIVE_FIRE_TYPE: RegistryObject<RecipeType<PrimitiveFireRecipe>> =
        TYPE_DEF_REG.register("primitive_fire", Supplier {
            object : RecipeType<PrimitiveFireRecipe> {
            }
        })

    @JvmField
    val PRIMITIVE_FIRE: RegistryObject<RecipeSerializer<PrimitiveFireRecipe>> =
        DEF_REG.register(
            "primitive_fire",
            Supplier { CookingRecipeSerializer(::PrimitiveFireRecipe, 200) })
}
