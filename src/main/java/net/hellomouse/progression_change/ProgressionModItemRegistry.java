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
}
