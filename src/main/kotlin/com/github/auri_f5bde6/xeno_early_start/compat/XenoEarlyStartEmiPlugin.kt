package com.github.auri_f5bde6.xeno_early_start.compat

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStartConfig
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartItemRegistry
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartRecipeRegistry
import dev.emi.emi.api.EmiEntrypoint
import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.block.Blocks
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.util.Identifier
import net.minecraftforge.common.Tags
import net.minecraftforge.registries.RegistryObject

@EmiEntrypoint
class XenoEarlyStartEmiPlugin : EmiPlugin {
    companion object {
        val RAW_BRICK: EmiStack = EmiStack.of(XenoEarlyStartItemRegistry.RAW_BRICK.get())
        val FIRE_STARTER: EmiStack = EmiStack.of(XenoEarlyStartItemRegistry.FIRE_STARTER.get())
        val FLINT_PICKAXE: EmiStack = EmiStack.of(XenoEarlyStartItemRegistry.FLINT_PICKAXE.get())
        val PEBBLE: EmiStack = EmiStack.of(XenoEarlyStartItemRegistry.PEBBLE.get())
        val PLANT_FIBER: EmiStack = EmiStack.of(XenoEarlyStartItemRegistry.PLANT_FIBER.get())
        val GRAVEL: EmiIngredient = EmiIngredient.of(Tags.Blocks.GRAVEL)
        val GRINDSTONE: EmiIngredient = EmiIngredient.of(Ingredient.ofItems(Blocks.GRINDSTONE.asItem()))

        val DRYING_CATEGORY = EmiRecipeCategory(
            XenoEarlyStart.of("drying"),
            RAW_BRICK,
            EmiTexture(XenoEarlyStart.of("textures/item/raw_brick.png"), 0, 0, 16, 16)
        )
        val FIRE_STARTER_CATEGORY = EmiRecipeCategory(
            XenoEarlyStart.of("world_interaction"),
            FIRE_STARTER,
            EmiTexture(XenoEarlyStart.of("textures/item/fire_starter.png"), 0, 0, 16, 16)
        )
        val BLOCK_TO_BLOCK_CATEGORY = EmiRecipeCategory(
            XenoEarlyStart.of("block_to_block"),
            FLINT_PICKAXE,
            EmiTexture(Identifier.of("minecraft", "textures/item/flint_pickaxe.png"), 0, 0, 16, 16)
        )
        val KNAPPING_CATEGORY = EmiRecipeCategory(
            XenoEarlyStart.of("knapping"),
            PEBBLE,
            EmiTexture(XenoEarlyStart.of("textures/item/pebble.png"), 0, 0, 16, 16)
        )
        val PLANT_FIBER_CATEGORY = EmiRecipeCategory(
            XenoEarlyStart.of("plant_fiber"),
            PLANT_FIBER,
            EmiTexture(XenoEarlyStart.of("textures/item/plant_fiber.png"), 0, 0, 16, 16)
        )
    }

    override fun register(registry: EmiRegistry) {
        registry.addCategory(DRYING_CATEGORY)
        registry.addWorkstation(DRYING_CATEGORY, RAW_BRICK)
        registry.addRecipe(DryingEmiRecipe())

        registry.addCategory(FIRE_STARTER_CATEGORY)
        registry.addWorkstation(FIRE_STARTER_CATEGORY, FIRE_STARTER)
        registry.addRecipe(FireStarterRecipe())

        registry.addCategory(BLOCK_TO_BLOCK_CATEGORY)
        for (recipe in registry.recipeManager.listAllOfType(XenoEarlyStartRecipeRegistry.BLOCK_TO_BLOCK_TYPE.get())) {
            if (recipe.isOreToStone && XenoEarlyStartConfig.config.oreChanges.oreToStone || !recipe.isOreToStone) {
                registry.addRecipe(EMIBlockToBlockRecipe(recipe))
            }
        }

        registry.addCategory(KNAPPING_CATEGORY)
        registry.addWorkstation(KNAPPING_CATEGORY, PEBBLE)
        registry.addRecipe(
            KnappingRecipe(
                "stone",
                XenoEarlyStartItemRegistry.PEBBLE.get(),
                getStack(XenoEarlyStartItemRegistry.KNAPPED_STONE)
            )
        )
        registry.addRecipe(
            KnappingRecipe(
                "deepslate",
                XenoEarlyStartItemRegistry.DEEPSLATE_PEBBLE.get(),
                getStack(XenoEarlyStartItemRegistry.KNAPPED_DEEPSLATE)
            )
        )
        registry.addRecipe(
            KnappingRecipe(
                "blackstone",
                XenoEarlyStartItemRegistry.BLACKSTONE_PEBBLE.get(),
                getStack(XenoEarlyStartItemRegistry.KNAPPED_BLACKSTONE)
            )
        )
        registry.addRecipe(
            KnappingRecipe(
                "flint",
                Items.FLINT,
                getStack(XenoEarlyStartItemRegistry.FLINT_SHARD, amount = 2)
            )
        )

        registry.addCategory(PLANT_FIBER_CATEGORY)
        registry.addWorkstation(PLANT_FIBER_CATEGORY, EmiStack.of(Items.FLINT))
        registry.addRecipe(PlantFiberRecipe())

        registry.addRecipe(
            EmiWorldInteractionRecipe.builder().id(XenoEarlyStart.of("/grind_gravel")).leftInput(
                GRAVEL
            ).rightInput(
                GRINDSTONE, true
            ).output(getStack(Items.FLINT, chance = 0.8f))
                .build()
        )
    }

    private fun getStack(item: Item, chance: Float = 1.0f, amount: Long = 1): EmiStack {
        val stack = EmiStack.of(item)
        stack.amount = amount
        stack.chance = chance
        return stack
    }
    private fun getStack(item: RegistryObject<Item>, chance: Float = 1.0f, amount: Long = 1): EmiStack {
        return getStack(item.get(), chance, amount)
    }
}