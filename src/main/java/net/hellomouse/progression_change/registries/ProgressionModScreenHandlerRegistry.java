package net.hellomouse.progression_change.registries;

import net.hellomouse.progression_change.ProgressionMod;
import net.hellomouse.progression_change.client.screen.BrickFurnaceScreenHandler;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ProgressionModScreenHandlerRegistry {
    public static final DeferredRegister<ScreenHandlerType<?>> DEF_REG = DeferredRegister.create(ForgeRegistries.MENU_TYPES, ProgressionMod.MODID);
    public static final RegistryObject<ScreenHandlerType<BrickFurnaceScreenHandler>> BRICK_FURNACE_SCREEN = DEF_REG.register("brick_furnace_screen",
            () -> new ScreenHandlerType<>(BrickFurnaceScreenHandler::new, FeatureFlags.DEFAULT_ENABLED_FEATURES)
    );
}
