package net.hellomouse.xeno_early_start

import net.fabricmc.yarn.constants.MiningLevels
import net.minecraft.item.Items
import net.minecraft.item.ToolMaterial
import net.minecraft.recipe.Ingredient
import net.minecraft.util.Identifier

object ProgressionModToolMaterials {
    val FLINT: ToolMaterial = Material(MiningLevels.WOOD, 48, 1.0f, 0f, 10, Ingredient.ofItems(Items.FLINT))
    val BONE: ToolMaterial = Material(MiningLevels.WOOD, 32, 1.0f, 0f, 10, Ingredient.ofItems(Items.FLINT))
    val COPPER: ToolMaterial = Material(MiningLevels.STONE, 190, 5.0f, 1.0f, 13, Ingredient.ofItems(Items.COPPER_INGOT))
    val FLINT_ID: Identifier? = ProgressionMod.Companion.of("flint")
    val BONE_ID: Identifier? = ProgressionMod.Companion.of("bone")
    val COPPER_ID: Identifier? = ProgressionMod.Companion.of("copper")

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
