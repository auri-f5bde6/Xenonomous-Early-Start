package net.hellomouse.xeno_early_start.compat

import dev.emi.emi.api.EmiEntrypoint
import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiStack
import net.hellomouse.xeno_early_start.ProgressionMod
import net.hellomouse.xeno_early_start.registries.ProgressionModItemRegistry

@EmiEntrypoint
class XenoEarlyStartEmiPlugin : EmiPlugin {
    companion object {
        val RAW_BRICK: EmiStack = EmiStack.of(ProgressionModItemRegistry.RAW_BRICK.get())
        val DRYING = EmiRecipeCategory(
            ProgressionMod.of("drying"),
            RAW_BRICK,
            EmiTexture(ProgressionMod.of("textures/item/raw_brick.png"), 0, 0, 16, 16)
        )
    }

    override fun register(registry: EmiRegistry) {
        registry.addCategory(DRYING)
        registry.addWorkstation(DRYING, RAW_BRICK)
        registry.addRecipe(DryingEmiRecipe())
    }
}