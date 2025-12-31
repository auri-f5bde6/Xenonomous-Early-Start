package com.github.auri_f5bde6.xeno_early_start.mixins;

import com.github.auri_f5bde6.xeno_early_start.item.PebbleItem;
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartItemRegistry;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Item.class)
public class ItemMixin {
    @Shadow
    @Final
    private RegistryEntry.Reference<Item> registryEntry;

    @WrapMethod(method = "useOnBlock")
    public ActionResult useOnBlock(ItemUsageContext context, Operation<ActionResult> original) {
        if (this.registryEntry.matchesKey(Items.FLINT.getRegistryEntry().registryKey())) {
            return PebbleItem.Companion.useOnBlock(context,
                    new ItemStack(XenoEarlyStartItemRegistry.FLINT_SHARD.get(),
                            context.getWorld().getRandom().nextBetween(2, 5))
            );
        }
        return original.call(context);
    }
}
