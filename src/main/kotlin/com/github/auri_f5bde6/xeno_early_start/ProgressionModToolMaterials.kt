package com.github.auri_f5bde6.xeno_early_start

import net.fabricmc.yarn.constants.MiningLevels
import net.minecraft.item.Items
import net.minecraft.item.ToolMaterial
import net.minecraft.recipe.Ingredient
import net.minecraft.util.Identifier

// Some code borrowed from vanilla ToolMaterials
object ProgressionModToolMaterials {
    @JvmField
    val FLINT: ToolMaterial = Material(MiningLevels.WOOD, 48, 1.0f, 0f, 10, Ingredient.ofItems(Items.FLINT))

    @JvmField
    val BONE: ToolMaterial = Material(MiningLevels.WOOD, 32, 1.0f, 0f, 10, Ingredient.ofItems(Items.FLINT))

    @JvmField
    val COPPER: ToolMaterial = Material(MiningLevels.STONE, 210, 5.0f, 1.0f, 13, Ingredient.ofItems(Items.COPPER_INGOT))

    @JvmField
    val FLINT_ID: Identifier = ProgressionMod.of("flint")

    @JvmField
    val BONE_ID: Identifier = ProgressionMod.of("bone")

    @JvmField
    val COPPER_ID: Identifier = ProgressionMod.of("copper")

    @JvmRecord
    private data class Material(
        val miningLevel: Int, val itemDurability: Int, val miningSpeed: Float, val attackDamage: Float,
        val enchantability: Int, val repairIngredient: Ingredient?
    ) : ToolMaterial {
        override fun getDurability(): Int {
            return this.itemDurability
        }

        override fun getMiningSpeedMultiplier(): Float {
            return this.miningSpeed
        }

        override fun getAttackDamage(): Float {
            return this.attackDamage
        }

        @Deprecated("Deprecated in Java")
        override fun getMiningLevel(): Int {
            return this.miningLevel
        }

        override fun getEnchantability(): Int {
            return this.enchantability
        }

        override fun getRepairIngredient(): Ingredient? {
            return this.repairIngredient
        }
    }
}
