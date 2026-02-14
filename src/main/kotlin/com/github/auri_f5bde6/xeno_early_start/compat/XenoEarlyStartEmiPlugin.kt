package com.github.auri_f5bde6.xeno_early_start.compat

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartItemRegistry
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartRecipeRegistry
import dev.emi.emi.api.EmiEntrypoint
import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.util.Identifier

@EmiEntrypoint
class XenoEarlyStartEmiPlugin : EmiPlugin {
    companion object {
        val RAW_BRICK: EmiStack = EmiStack.of(XenoEarlyStartItemRegistry.RAW_BRICK.get())
        val FIRE_STARTER: EmiStack = EmiStack.of(XenoEarlyStartItemRegistry.FIRE_STARTER.get())
        val FLINT_PICKAXE: EmiStack = EmiStack.of(XenoEarlyStartItemRegistry.FLINT_PICKAXE.get())
        val PEBBLE: EmiStack = EmiStack.of(XenoEarlyStartItemRegistry.PEBBLE.get())
        val DRYING = EmiRecipeCategory(
            XenoEarlyStart.of("drying"),
            RAW_BRICK,
            EmiTexture(XenoEarlyStart.of("textures/item/raw_brick.png"), 0, 0, 16, 16)
        )
        val WORLD_INTERACTION = EmiRecipeCategory(
            XenoEarlyStart.of("world_interaction"),
            FIRE_STARTER,
            EmiTexture(XenoEarlyStart.of("textures/item/fire_starter.png"), 0, 0, 16, 16)
        )
        val BLOCK_TO_BLOCK = EmiRecipeCategory(
            XenoEarlyStart.of("block_to_block"),
            FLINT_PICKAXE,
            EmiTexture(Identifier.of("minecraft", "textures/item/flint_pickaxe.png"), 0, 0, 16, 16)
        )
        val KNAPPING = EmiRecipeCategory(
            XenoEarlyStart.of("knapping"),
            FIRE_STARTER,
            EmiTexture(XenoEarlyStart.of("textures/item/pebble.png"), 0, 0, 16, 16)
        )
    }

    override fun register(registry: EmiRegistry) {
        registry.addCategory(DRYING)
        registry.addWorkstation(DRYING, RAW_BRICK)
        registry.addRecipe(DryingEmiRecipe())

        registry.addCategory(WORLD_INTERACTION)
        registry.addWorkstation(WORLD_INTERACTION, FIRE_STARTER)
        registry.addRecipe(WorldInteractionRecipe())

        registry.addCategory(BLOCK_TO_BLOCK)
        for (recipe in registry.recipeManager.listAllOfType(XenoEarlyStartRecipeRegistry.BLOCK_TO_BLOCK_TYPE.get())) {
            registry.addRecipe(EMIBlockToBlockRecipe(recipe))
        }

        registry.addCategory(KNAPPING)
        registry.addWorkstation(KNAPPING, PEBBLE)
        registry.addRecipe(
            KnappingRecipe(
                XenoEarlyStartItemRegistry.PEBBLE.get(),
                XenoEarlyStartItemRegistry.KNAPPED_STONE.get()
            )
        )
        registry.addRecipe(
            KnappingRecipe(
                XenoEarlyStartItemRegistry.DEEPSLATE_PEBBLE.get(),
                XenoEarlyStartItemRegistry.KNAPPED_DEEPSLATE.get()
            )
        )
        registry.addRecipe(
            KnappingRecipe(
                XenoEarlyStartItemRegistry.BLACKSTONE_PEBBLE.get(),
                XenoEarlyStartItemRegistry.KNAPPED_BLACKSTONE.get()
            )
        )
    }
}