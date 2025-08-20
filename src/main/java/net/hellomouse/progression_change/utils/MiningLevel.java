package net.hellomouse.progression_change.utils;

import net.hellomouse.progression_change.ProgressionModConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraftforge.common.TierSortingRegistry;

public class MiningLevel {
    public static boolean IsToolLowerThanTier(ItemStack itemStack, ToolMaterial lowerThanTier) {
        if (itemStack == null) {
            return false;
        } else if (itemStack.getItem() instanceof MiningToolItem miningToolItem) {
            var toolMaterial = miningToolItem.getMaterial();
            if (TierSortingRegistry.isTierSorted(toolMaterial)) {
                return TierSortingRegistry.getTiersLowerThan(lowerThanTier).contains(toolMaterial);
            } else {
                return toolMaterial.getMiningLevel() < lowerThanTier.getMiningLevel() && ProgressionModConfig.oreDropChanges.moddedPickaxeWorkaround;
            }
        }
        return false;
    }
}
