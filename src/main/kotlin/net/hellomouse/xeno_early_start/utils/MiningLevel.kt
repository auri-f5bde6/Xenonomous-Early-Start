package net.hellomouse.xeno_early_start.utils

import net.hellomouse.xeno_early_start.mixins.TierSortingRegistryAccessor
import net.minecraft.item.ItemStack
import net.minecraft.item.MiningToolItem
import net.minecraft.item.ToolMaterial
import net.minecraftforge.common.TierSortingRegistry

object MiningLevel {
    private fun isTierPredicate(
        itemStack: ItemStack?,
        tier: ToolMaterial?,
        predicate: (ToolMaterial, ToolMaterial) -> Boolean
    ): Boolean {
        val item = itemStack?.item
        if (itemStack == null || tier == null) {
            return false
        } else if (item is MiningToolItem) {
            val toolMaterial: ToolMaterial = item.material
            return predicate(toolMaterial, tier)
        }
        return false
    }

    fun isToolEqualToTier(itemStack: ItemStack?, tier: ToolMaterial?): Boolean {
        return isTierPredicate(itemStack, tier) { toolMaterial, tier ->
            toolMaterial == tier
        }
    }

    fun isToolLowerThanTier(itemStack: ItemStack?, lowerThanTier: ToolMaterial?): Boolean {
        return isTierPredicate(itemStack, lowerThanTier) { toolMaterial, lowerThanTier ->
            TierSortingRegistry.getTiersLowerThan(lowerThanTier).contains(toolMaterial)
        }
    }

    fun isToolLowerOrEqualThanTier(itemStack: ItemStack?, lowerOrEqualThanTier: ToolMaterial?): Boolean {
        return isTierPredicate(itemStack, lowerOrEqualThanTier) { toolMaterial, lowerThanOrEqualTier ->
            TierSortingRegistry.getTiersLowerThan(lowerThanOrEqualTier)
                .contains(toolMaterial) || toolMaterial == lowerThanOrEqualTier
        }
    }

    fun isToolGreaterThanTier(itemStack: ItemStack?, greaterThanTier: ToolMaterial?): Boolean {
        return isTierPredicate(itemStack, greaterThanTier) { toolMaterial, greaterThanTier ->
            getTiersGreaterThan(greaterThanTier).contains(toolMaterial)
        }
    }

    fun isToolGreaterOrEqualThanTier(itemStack: ItemStack?, greaterOrEqualThanTier: ToolMaterial?): Boolean {
        return isTierPredicate(itemStack, greaterOrEqualThanTier) { toolMaterial, greaterOrEqualThanTier ->
            getTiersGreaterThan(greaterOrEqualThanTier).contains(toolMaterial)
        }
    }

    fun getTiersGreaterThan(tier: ToolMaterial?): MutableList<ToolMaterial?> {
        if (!TierSortingRegistry.isTierSorted(tier)) return mutableListOf()
        return TierSortingRegistryAccessor.getSortedTiers().reversed().stream()
            .takeWhile { t: ToolMaterial? -> t !== tier }.toList()
    }

    fun getTierName(material: ToolMaterial?): String {
        val name = TierSortingRegistry.getName(material)
        if (name == null) {
            throw IllegalArgumentException(
                "Pickaxe Tier $material is missing from tierSortingRegistry!",
            )
        } else {
            return name.toString()
        }
    }
}
