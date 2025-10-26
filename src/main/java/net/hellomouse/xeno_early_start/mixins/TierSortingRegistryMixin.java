package net.hellomouse.xeno_early_start.mixins;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.TierSortingRegistry;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(value = TierSortingRegistry.class)
public class TierSortingRegistryMixin {
    @WrapMethod(method = "processTier")
    private static void processTier(ToolMaterial tier, Identifier name, List<Object> afters, List<Object> befores, Operation<Void> original) {
        if (tier == ToolMaterials.GOLD) {
            original.call(tier, name, List.of(new Identifier("iron")), List.of(new Identifier("diamond")));
        } else {
            original.call(tier, name, afters, befores);
        }
    }
}
