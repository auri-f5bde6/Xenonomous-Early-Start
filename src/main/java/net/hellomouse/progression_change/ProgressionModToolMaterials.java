package net.hellomouse.progression_change;

import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

public class ProgressionModToolMaterials {

    public static final ToolMaterial FLINT = new Material(MiningLevels.WOOD, 48, 1.0f, 0f, 10, Ingredient.ofItems(Items.FLINT));
    public static final ToolMaterial BONE = new Material(MiningLevels.WOOD, 32, 1.0f, 0f, 10, Ingredient.ofItems(Items.FLINT));
    public static final ToolMaterial COPPER = new Material(MiningLevels.STONE, 190, 5.0f, 1.0f, 13, Ingredient.ofItems(Items.COPPER_INGOT));
    public static final Identifier FLINT_ID = ProgressionMod.of("flint");
    public static final Identifier BONE_ID = ProgressionMod.of("bone");
    public static final Identifier COPPER_ID = ProgressionMod.of("copper");

    private record Material(int miningLevel, int itemDurability, float miningSpeed, float attackDamage,
                            int enchantability, Ingredient repairIngredient) implements ToolMaterial {

        @Override
        public int getDurability() {
            return this.itemDurability;
        }

        @Override
        public float getMiningSpeedMultiplier() {
            return this.miningSpeed;
        }

        @Override
        public float getAttackDamage() {
            return this.attackDamage;
        }

        @Override
        public int getMiningLevel() {
            return this.miningLevel;
        }

        @Override
        public int getEnchantability() {
            return this.enchantability;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return this.repairIngredient;
        }
    }
}
