package net.hellomouse.progression_change;

import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ProgressionModItemRegistry {
    public static final DeferredRegister<Item> DEF_REG = DeferredRegister.create(ForgeRegistries.ITEMS, ProgressionMod.MODID);
    public static final RegistryObject<Item> COPPER_SWORD = DEF_REG.register("copper_sword", () ->
            new PickaxeItem(ProgressionModToolMaterials.COPPER, 3, -2.4f, new Item.Settings()) // The attackDamage is actually 1.5 in the snapshot
    );
    public static final RegistryObject<Item> COPPER_SHOVEL = DEF_REG.register("copper_shovel", () ->
            new PickaxeItem(ProgressionModToolMaterials.COPPER, 1, -2.0f, new Item.Settings()) // The attackDamage is actually 1.5 in the snapshot
    );
    public static final RegistryObject<Item> COPPER_PICKAXE = DEF_REG.register("copper_pickaxe", () ->
            new PickaxeItem(ProgressionModToolMaterials.COPPER, 1, -2.8f, new Item.Settings())
    );
    public static final RegistryObject<Item> COPPER_AXE = DEF_REG.register("copper_axe", () ->
            new PickaxeItem(ProgressionModToolMaterials.COPPER, 7, -3.2f, new Item.Settings())
    );
    public static final RegistryObject<Item> COPPER_HOE = DEF_REG.register("copper_hoe", () ->
            new PickaxeItem(ProgressionModToolMaterials.COPPER, -1, -2.0f, new Item.Settings())
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
}
