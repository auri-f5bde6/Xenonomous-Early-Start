package net.hellomouse.xeno_early_start

import com.google.common.base.Supplier
import com.google.common.base.Suppliers
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.ArmorMaterials
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.Util
import java.util.*

enum class ProgressionModArmourMaterials(
    private val enumName: String,
    private val durabilityMultiplier: Int,
    private val protectionAmounts: EnumMap<ArmorItem.Type, Int>,
    private val enchantability: Int,
    private val equipSound: SoundEvent,
    private val toughness: Float,
    private val knockbackResistance: Float,
    repairIngredientSupplier: Supplier<Ingredient?>
) : StringIdentifiable, ArmorMaterial {
    COPPER(
        "copper",
        11,
        Util.make<EnumMap<ArmorItem.Type, Int>>(
            EnumMap<ArmorItem.Type, Int>(ArmorItem.Type::class.java)
        ) { map: EnumMap<ArmorItem.Type, Int> ->
            map.put(ArmorItem.Type.BOOTS, 1)
            map.put(ArmorItem.Type.LEGGINGS, 3)
            map.put(ArmorItem.Type.CHESTPLATE, 4)
            map.put(ArmorItem.Type.HELMET, 2)
        },
        8,
        SoundEvents.ITEM_ARMOR_EQUIP_IRON,
        0.0f,
        0.0f,
        Supplier { Ingredient.ofItems(Items.COPPER_INGOT) }), ;

    private val repairIngredientSupplier: Supplier<Ingredient?> = Suppliers.memoize(
        repairIngredientSupplier
    )

    override fun getDurability(type: ArmorItem.Type?): Int {
        return ArmorMaterials.BASE_DURABILITY.get(type)!! * this.durabilityMultiplier
    }

    override fun getProtection(type: ArmorItem.Type?): Int {
        return this.protectionAmounts.get(type)!!
    }

    override fun getEnchantability(): Int {
        return this.enchantability
    }

    override fun getEquipSound(): SoundEvent? {
        return this.equipSound
    }

    override fun getRepairIngredient(): Ingredient? {
        return this.repairIngredientSupplier.get()
    }

    override fun getName(): String {
        return this.enumName
    }

    override fun getToughness(): Float {
        return this.toughness
    }

    override fun getKnockbackResistance(): Float {
        return this.knockbackResistance
    }

    override fun asString(): String {
        return this.enumName
    }

    companion object {
        val CODEC: StringIdentifiable.Codec<ArmorMaterials?> =
            StringIdentifiable.createCodec<ArmorMaterials?> { ArmorMaterials.entries.toTypedArray() }
    }
}
