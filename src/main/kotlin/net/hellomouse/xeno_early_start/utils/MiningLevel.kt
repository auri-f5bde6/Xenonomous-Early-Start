package net.hellomouse.xeno_early_start.utils

import net.hellomouse.xeno_early_start.ProgressionMod
import net.hellomouse.xeno_early_start.ProgressionModConfig
import net.minecraft.item.ItemStack
import net.minecraft.item.MiningToolItem
import net.minecraft.item.ToolMaterial
import net.minecraftforge.common.TierSortingRegistry

object MiningLevel {
    fun IsToolLowerThanTier(itemStack: ItemStack?, lowerThanTier: ToolMaterial?): Boolean {
        val item = itemStack?.item
        if (itemStack == null || lowerThanTier == null) {
            return false
        } else if (item is MiningToolItem) {
            val toolMaterial: ToolMaterial = item.material
            if (TierSortingRegistry.isTierSorted(toolMaterial)) {
                return TierSortingRegistry.getTiersLowerThan(lowerThanTier).contains(toolMaterial)
            } else {
                return toolMaterial.miningLevel < lowerThanTier.miningLevel && ProgressionModConfig.oreDropChanges.moddedPickaxeWorkaround
            }
        }
        return false
    }

    fun getTierName(material: ToolMaterial?): String? {
        val name = TierSortingRegistry.getName(material)
        if (name == null) {
            ProgressionMod.Companion.LOGGER.warn(
                "Pickaxe Tier {} is missing from tierSortingRegistry! (pickaxe_tier's below_tier will fallback to minecraft:wood)",
                material
            )
            return "minecraft:wood"
        } else {
            return name.toString()
        }
    }
}
