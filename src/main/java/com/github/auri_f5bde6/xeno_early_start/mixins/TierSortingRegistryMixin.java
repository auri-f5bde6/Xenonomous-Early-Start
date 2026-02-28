package com.github.auri_f5bde6.xeno_early_start.mixins;

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStartToolMaterials;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.BlockState;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.Tags;
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

    @WrapMethod(method = "isCorrectTierForDrops")
    private static boolean flintCantMineOre(ToolMaterial tier, BlockState state, Operation<Boolean> original) {
        if ((tier == XenoEarlyStartToolMaterials.FLINT || tier == XenoEarlyStartToolMaterials.BONE) && state.isIn(Tags.Blocks.ORES)) {
            // Flint/bone pickaxe cannot mine ore
            return false;
        } else if (tier == XenoEarlyStartToolMaterials.COPPER && (state.isIn(Tags.Blocks.ORES) && !(state.isIn(Tags.Blocks.ORES_COPPER) || state.isIn(Tags.Blocks.ORES_IRON)))) {
            // Copper pickaxe can only mine iron or copper (todo: and silver)
            return false;
        }
        return original.call(tier, state);
    }
}
