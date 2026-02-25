package com.github.auri_f5bde6.xeno_early_start.registries

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.recipe.Serializer
import com.github.auri_f5bde6.xeno_early_start.recipe.StoneToCobbleRecipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.RegistryKeys
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier

object XenoEarlyStartRecipeRegistry {
    val TYPE_DEF_REG: DeferredRegister<RecipeType<*>> =
        DeferredRegister.create(RegistryKeys.RECIPE_TYPE, XenoEarlyStart.MODID)
    val DEF_REG: DeferredRegister<RecipeSerializer<*>> =
        DeferredRegister.create(RegistryKeys.RECIPE_SERIALIZER, XenoEarlyStart.MODID)

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
}
