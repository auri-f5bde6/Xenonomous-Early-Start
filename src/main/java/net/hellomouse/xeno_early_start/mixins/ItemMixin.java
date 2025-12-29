package net.hellomouse.xeno_early_start.mixins;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.hellomouse.xeno_early_start.item.PebbleItem;
import net.hellomouse.xeno_early_start.registries.ProgressionModItemRegistry;
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
                    new ItemStack(ProgressionModItemRegistry.FLINT_SHARD.get(),
                            context.getWorld().getRandom().nextBetween(2, 5))
            );
        }
        return original.call(context);
    }
}
