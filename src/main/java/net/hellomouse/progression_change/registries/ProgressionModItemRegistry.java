package net.hellomouse.progression_change.registries;

import net.hellomouse.progression_change.ProgressionMod;
import net.hellomouse.progression_change.ProgressionModArmourMaterials;
import net.hellomouse.progression_change.ProgressionModToolMaterials;
import net.hellomouse.progression_change.item.BrickItem;
import net.minecraft.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ProgressionModItemRegistry {
    public static final DeferredRegister<Item> VANILLA_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "minecraft");
    public static final DeferredRegister<Item> DEF_REG = DeferredRegister.create(ForgeRegistries.ITEMS, ProgressionMod.MODID);
    public static final RegistryObject<Item> COPPER_SWORD = DEF_REG.register("copper_sword", () ->
            new SwordItem(ProgressionModToolMaterials.COPPER, 3, -2.4f, new Item.Settings()) // The attackDamage is actually 1.5 in the snapshot
    );
    public static final RegistryObject<Item> COPPER_SHOVEL = DEF_REG.register("copper_shovel", () ->
            new ShovelItem(ProgressionModToolMaterials.COPPER, 1, -2.0f, new Item.Settings()) // The attackDamage is actually 1.5 in the snapshot
    );
    public static final RegistryObject<Item> COPPER_PICKAXE = DEF_REG.register("copper_pickaxe", () ->
            new PickaxeItem(ProgressionModToolMaterials.COPPER, 1, -2.8f, new Item.Settings())
    );
    public static final RegistryObject<Item> COPPER_AXE = DEF_REG.register("copper_axe", () ->
            new AxeItem(ProgressionModToolMaterials.COPPER, 7, -3.2f, new Item.Settings())
    );
    public static final RegistryObject<Item> COPPER_HOE = DEF_REG.register("copper_hoe", () ->
            new HoeItem(ProgressionModToolMaterials.COPPER, -1, -2.0f, new Item.Settings())
    );
    public static final RegistryObject<Item> COPPER_HELMET = DEF_REG.register("copper_helmet", () ->
            new ArmorItem(ProgressionModArmourMaterials.COPPER, ArmorItem.Type.HELMET, new Item.Settings())
    );
    public static final RegistryObject<Item> COPPER_CHESTPLATE = DEF_REG.register("copper_chestplate", () ->
            new ArmorItem(ProgressionModArmourMaterials.COPPER, ArmorItem.Type.CHESTPLATE, new Item.Settings())
    );
    public static final RegistryObject<Item> COPPER_LEGGINGS = DEF_REG.register("copper_leggings", () ->
            new ArmorItem(ProgressionModArmourMaterials.COPPER, ArmorItem.Type.LEGGINGS, new Item.Settings())
    );
    public static final RegistryObject<Item> COPPER_BOOTS = DEF_REG.register("copper_boots", () ->
            new ArmorItem(ProgressionModArmourMaterials.COPPER, ArmorItem.Type.BOOTS, new Item.Settings())
    );
    public static final RegistryObject<Item> FLINT_PICKAXE = DEF_REG.register("flint_pickaxe", () ->
            new PickaxeItem(ProgressionModToolMaterials.FLINT, 1, -2.8f, new Item.Settings())
    );
    public static final RegistryObject<Item> BONE_PICKAXE = DEF_REG.register("bone_pickaxe", () ->
            new PickaxeItem(ProgressionModToolMaterials.BONE, 1, -2.8f, new Item.Settings())
    );
    public static final RegistryObject<Item> COPPER_NUGGET = DEF_REG.register("copper_nugget", () ->
            new Item(new Item.Settings())
    );
    public static final RegistryObject<Item> RAW_COPPER_NUGGET = DEF_REG.register("raw_copper_nugget", () ->
            new Item(new Item.Settings())
    );
    public static final RegistryObject<Item> RAW_IRON_NUGGET = DEF_REG.register("raw_iron_nugget", () ->
            new Item(new Item.Settings())
    );
    public static final RegistryObject<Item> RAW_GOLD_NUGGET = DEF_REG.register("raw_gold_nugget", () ->
            new Item(new Item.Settings())
    );
    public static final RegistryObject<Item> DIAMOND_FRAGMENT = DEF_REG.register("diamond_fragment", () ->
            new Item(new Item.Settings())
    );
    public static final RegistryObject<Item> RAW_BRICK = DEF_REG.register("raw_brick", () ->
            new BlockItem(ProgressionModBlockRegistry.RAW_BRICK.get(), new Item.Settings())
    );
    public static final RegistryObject<Item> FLINT_HATCHET = DEF_REG.register("flint_hatchet", () ->
            new AxeItem(ProgressionModToolMaterials.FLINT, 6.0f, -3.2f, new Item.Settings())
    );
    public static final RegistryObject<Item> PLANT_FIBER = DEF_REG.register("plant_fiber", () ->
            new Item(new Item.Settings())
    );
    public static final RegistryObject<Item> BRICK = VANILLA_ITEMS.register("brick", () ->
            new BrickItem(new Item.Settings())
    );
    public static final List<RegistryObject<Item>> CREATIVE_MOD_TAB = List.of(
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
            PLANT_FIBER
    );

    public static void addItemToCreativeTab(ItemGroup.Entries entries) {
        for (var i : ProgressionModItemRegistry.CREATIVE_MOD_TAB) {
            entries.add(i.get());
        }
    }
}
