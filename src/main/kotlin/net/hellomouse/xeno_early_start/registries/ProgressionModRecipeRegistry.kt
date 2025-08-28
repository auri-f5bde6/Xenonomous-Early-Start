package net.hellomouse.xeno_early_start.registries

import net.hellomouse.xeno_early_start.ProgressionMod
import net.hellomouse.xeno_early_start.recipe.StoneToCobbleRecipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.RegistryKeys
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier

object ProgressionModRecipeRegistry {
    val TYPE_DEF_REG: DeferredRegister<RecipeType<*>> =
        DeferredRegister.create(RegistryKeys.RECIPE_TYPE, ProgressionMod.Companion.MODID)
    val DEF_REG: DeferredRegister<RecipeSerializer<*>> =
        DeferredRegister.create(RegistryKeys.RECIPE_SERIALIZER, ProgressionMod.Companion.MODID)

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
            Supplier { StoneToCobbleRecipe.Serializer() })
}
