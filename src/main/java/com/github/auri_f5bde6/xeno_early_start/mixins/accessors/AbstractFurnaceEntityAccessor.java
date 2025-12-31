package com.github.auri_f5bde6.xeno_early_start.mixins.accessors;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractFurnaceBlockEntity.class)
public interface AbstractFurnaceEntityAccessor {
    @Invoker("getFuelTime")
    int xeno_early_start$getFuelTime(ItemStack fuel);

    @Accessor("inventory")
    DefaultedList<ItemStack> xeno_early_start$getInventory();

}