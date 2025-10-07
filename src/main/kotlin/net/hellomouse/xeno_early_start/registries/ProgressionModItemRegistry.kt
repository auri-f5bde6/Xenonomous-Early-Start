package net.hellomouse.xeno_early_start.registries

import net.hellomouse.xeno_early_start.ProgressionMod
import net.hellomouse.xeno_early_start.ProgressionModArmourMaterials
import net.hellomouse.xeno_early_start.ProgressionModToolMaterials
import net.hellomouse.xeno_early_start.item.BrickItem
import net.hellomouse.xeno_early_start.item.FireStarterItem
import net.hellomouse.xeno_early_start.item.PebbleItem
import net.minecraft.item.*
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier

object ProgressionModItemRegistry {
    val VANILLA_ITEMS: DeferredRegister<Item> = DeferredRegister.create<Item>(ForgeRegistries.ITEMS, "minecraft")
    val DEF_REG: DeferredRegister<Item> =
        DeferredRegister.create(ForgeRegistries.ITEMS, ProgressionMod.Companion.MODID)

    @JvmField
    val COPPER_SWORD: RegistryObject<Item> = DEF_REG.register(
        "copper_sword",
        Supplier {
            SwordItem(
                ProgressionModToolMaterials.COPPER,
                3,
                -2.4f,
                Item.Settings()
            )
        } // The attackDamage is actually 1.5 in the snapshot
    )

    @JvmField
    val COPPER_SHOVEL: RegistryObject<Item> = DEF_REG.register(
        "copper_shovel",
        Supplier {
            ShovelItem(
                ProgressionModToolMaterials.COPPER,
                1f,
                -2.0f,
                Item.Settings()
            )
        } // The attackDamage is actually 1.5 in the snapshot
    )

    @JvmField
    val COPPER_PICKAXE: RegistryObject<Item> = DEF_REG.register(
        "copper_pickaxe",
        Supplier { PickaxeItem(ProgressionModToolMaterials.COPPER, 1, -2.8f, Item.Settings()) }
    )

    @JvmField
    val COPPER_AXE: RegistryObject<Item> = DEF_REG.register(
        "copper_axe",
        Supplier { AxeItem(ProgressionModToolMaterials.COPPER, 7f, -3.2f, Item.Settings()) }
    )

    @JvmField
    val COPPER_HOE: RegistryObject<Item> = DEF_REG.register(
        "copper_hoe",
        Supplier { HoeItem(ProgressionModToolMaterials.COPPER, -1, -2.0f, Item.Settings()) }
    )

    @JvmField
    val COPPER_HELMET: RegistryObject<Item> = DEF_REG.register(
        "copper_helmet",
        Supplier { ArmorItem(ProgressionModArmourMaterials.COPPER, ArmorItem.Type.HELMET, Item.Settings()) }
    )

    @JvmField
    val COPPER_CHESTPLATE: RegistryObject<Item> = DEF_REG.register(
        "copper_chestplate",
        Supplier { ArmorItem(ProgressionModArmourMaterials.COPPER, ArmorItem.Type.CHESTPLATE, Item.Settings()) }
    )

    @JvmField
    val COPPER_LEGGINGS: RegistryObject<Item> = DEF_REG.register(
        "copper_leggings",
        Supplier { ArmorItem(ProgressionModArmourMaterials.COPPER, ArmorItem.Type.LEGGINGS, Item.Settings()) }
    )

    @JvmField
    val COPPER_BOOTS: RegistryObject<Item> = DEF_REG.register(
        "copper_boots",
        Supplier { ArmorItem(ProgressionModArmourMaterials.COPPER, ArmorItem.Type.BOOTS, Item.Settings()) }
    )

    @JvmField
    val FLINT_HATCHET: RegistryObject<Item> = DEF_REG.register(
        "flint_hatchet",
        Supplier { AxeItem(ProgressionModToolMaterials.FLINT, 6.0f, -3.2f, Item.Settings()) }
    )

    @JvmField
    val FLINT_PICKAXE: RegistryObject<Item> = DEF_REG.register(
        "flint_pickaxe",
        Supplier { PickaxeItem(ProgressionModToolMaterials.FLINT, 1, -2.8f, Item.Settings()) }
    )

    @JvmField
    val FLINT_DAGGER: RegistryObject<Item> = DEF_REG.register(
        "flint_dagger",
        Supplier { SwordItem(ProgressionModToolMaterials.FLINT, 3, -2.4f, Item.Settings()) }
    )

    @JvmField
    val BONE_PICKAXE: RegistryObject<Item> = DEF_REG.register(
        "bone_pickaxe",
        Supplier { PickaxeItem(ProgressionModToolMaterials.BONE, 1, -2.8f, Item.Settings()) }
    )

    @JvmField
    val BONE_DAGGER: RegistryObject<Item> = DEF_REG.register(
        "bone_dagger",
        Supplier { SwordItem(ProgressionModToolMaterials.FLINT, 3, -2.4f, Item.Settings()) }
    )

    @JvmField
    val COPPER_NUGGET: RegistryObject<Item> =
        DEF_REG.register("copper_nugget", Supplier { Item(Item.Settings()) }
        )

    @JvmField
    val RAW_COPPER_NUGGET: RegistryObject<Item> =
        DEF_REG.register("raw_copper_nugget", Supplier { Item(Item.Settings()) }
        )

    @JvmField
    val RAW_IRON_NUGGET: RegistryObject<Item> =
        DEF_REG.register("raw_iron_nugget", Supplier { Item(Item.Settings()) }
        )

    @JvmField
    val RAW_GOLD_NUGGET: RegistryObject<Item> =
        DEF_REG.register("raw_gold_nugget", Supplier { Item(Item.Settings()) }
        )

    @JvmField
    val DIAMOND_FRAGMENT: RegistryObject<Item> =
        DEF_REG.register("diamond_fragment", Supplier { Item(Item.Settings()) }
        )

    @JvmField
    val RAW_BRICK: RegistryObject<Item> = DEF_REG.register(
        "raw_brick",
        Supplier { BlockItem(ProgressionModBlockRegistry.RAW_BRICK.get(), Item.Settings()) }
    )

    @JvmField
    val PLANT_FIBER: RegistryObject<Item> = DEF_REG.register("plant_fiber", Supplier { Item(Item.Settings()) }
    )

    @JvmField
    val KNAPPED_STONE: RegistryObject<Item> =
        DEF_REG.register("knapped_stone", Supplier { Item(Item.Settings()) }
        )

    @JvmField
    val PEBBLE: RegistryObject<Item> =
        DEF_REG.register("pebble", Supplier { PebbleItem(KNAPPED_STONE.get(), Item.Settings()) }
        )

    @JvmField
    val KNAPPED_DEEPSLATE: RegistryObject<Item> =
        DEF_REG.register("knapped_deepslate", Supplier { Item(Item.Settings()) }
        )

    @JvmField
    val DEEPSLATE_PEBBLE: RegistryObject<Item> =
        DEF_REG.register("deepslate_pebble", Supplier { PebbleItem(KNAPPED_DEEPSLATE.get(), Item.Settings()) }
        )

    @JvmField
    val KNAPPED_BLACKSTONE: RegistryObject<Item> =
        DEF_REG.register("knapped_blackstone", Supplier { Item(Item.Settings()) }
        )

    @JvmField
    val BLACKSTONE_PEBBLE: RegistryObject<Item> =
        DEF_REG.register("blackstone_pebble", Supplier { PebbleItem(KNAPPED_BLACKSTONE.get(), Item.Settings()) }
        )

    @JvmField
    val BRICK_FURNACE: RegistryObject<Item> = DEF_REG.register(
        "brick_furnace",
        Supplier { BlockItem(ProgressionModBlockRegistry.BRICK_FURNACE.get(), Item.Settings()) }
    )

    @JvmField
    val FIRE_STARTER: RegistryObject<Item> = DEF_REG.register(
        "fire_starter",
        Supplier { FireStarterItem(Item.Settings()) }
    )

    @JvmField
    val PRIMITIVE_FIRE: RegistryObject<Item> = DEF_REG.register(
        "primitive_fire",
        Supplier { BlockItem(ProgressionModBlockRegistry.PRIMITIVE_FIRE.get(), Item.Settings()) }
    )

    @JvmField
    val CREATIVE_MOD_TAB: List<RegistryObject<Item>> = listOf(
        COPPER_SWORD,
        COPPER_SHOVEL,
        COPPER_PICKAXE,
        COPPER_AXE,
        COPPER_HOE,
        COPPER_HELMET,
        COPPER_CHESTPLATE,
        COPPER_LEGGINGS,
        COPPER_BOOTS,
        FLINT_PICKAXE,
        BONE_PICKAXE,
        COPPER_NUGGET,
        RAW_COPPER_NUGGET,
        RAW_IRON_NUGGET,
        RAW_GOLD_NUGGET,
        DIAMOND_FRAGMENT,
        RAW_BRICK,
        FLINT_HATCHET,
        PLANT_FIBER,
        PEBBLE,
        DEEPSLATE_PEBBLE,
        BLACKSTONE_PEBBLE,
        KNAPPED_STONE,
        KNAPPED_DEEPSLATE,
        KNAPPED_BLACKSTONE,
        BRICK_FURNACE,
        FIRE_STARTER,
        PRIMITIVE_FIRE,
        FLINT_DAGGER,
        BONE_DAGGER,
    )

    @Suppress("UNUSED")
    @JvmField
    val BRICK: RegistryObject<Item> = VANILLA_ITEMS.register(
        "brick",
        Supplier { BrickItem(ProgressionModBlockRegistry.BRICK.get(), Item.Settings()) }
    )

    fun addItemToCreativeTab(entries: ItemGroup.Entries) {
        for (i in CREATIVE_MOD_TAB) {
            entries.add(i.get())
        }
    }
}
