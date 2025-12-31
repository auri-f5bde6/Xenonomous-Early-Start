package com.github.auri_f5bde6.xeno_early_start.mixins.accessors;

import net.minecraft.item.ToolMaterial;
import net.minecraftforge.common.TierSortingRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(TierSortingRegistry.class)
public interface TierSortingRegistryAccessor {
    @Accessor(remap = false, value = "sortedTiers")
    static List<ToolMaterial> xeno_early_start$getSortedTiers() {
        throw new AssertionError();
    }
}
