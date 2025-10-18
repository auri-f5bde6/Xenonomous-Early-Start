package net.hellomouse.xeno_early_start.mixins;

import net.minecraft.item.ToolMaterial;
import net.minecraftforge.common.TierSortingRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(TierSortingRegistry.class)
public interface TierSortingRegistryAccessor {
    @Accessor
    static List<ToolMaterial> getSortedTiers() {
        throw new AssertionError();
    }
}
