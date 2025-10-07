package net.hellomouse.xeno_early_start.registries

import net.hellomouse.xeno_early_start.ProgressionMod
import net.hellomouse.xeno_early_start.client.screen.BrickFurnaceScreenHandler
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.resource.featuretoggle.FeatureFlags
import net.minecraft.screen.ScreenHandlerType
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier

object ProgressionModScreenHandlerRegistry {
    val DEF_REG: DeferredRegister<ScreenHandlerType<*>> =
        DeferredRegister.create(ForgeRegistries.MENU_TYPES, ProgressionMod.MODID)

    @JvmField
    val BRICK_FURNACE_SCREEN: RegistryObject<ScreenHandlerType<BrickFurnaceScreenHandler>> =
        DEF_REG.register(
            "brick_furnace_screen",
            Supplier {
                ScreenHandlerType<BrickFurnaceScreenHandler>({ syncId: Int, playerInventory: PlayerInventory ->
                    BrickFurnaceScreenHandler(
                        syncId,
                        playerInventory
                    )
                }, FeatureFlags.DEFAULT_ENABLED_FEATURES)
            }
        )
}
