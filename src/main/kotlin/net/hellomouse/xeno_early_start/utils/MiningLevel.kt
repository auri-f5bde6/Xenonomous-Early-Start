package net.hellomouse.xeno_early_start.utils;

import net.hellomouse.xeno_early_start.ProgressionMod;
import net.hellomouse.xeno_early_start.ProgressionModConfig;
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

    public static String getTierName(ToolMaterial material) {
        var name = TierSortingRegistry.getName(material);
        if (name == null) {
            ProgressionMod.LOGGER.warn("Pickaxe Tier {} is missing from tierSortingRegistry! (pickaxe_tier's below_tier will fallback to minecraft:wood)", material);
            return "minecraft:wood";
        } else {
            return name.toString();
        }
    }
}
