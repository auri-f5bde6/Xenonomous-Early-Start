package com.github.auri_f5bde6.xeno_early_start.mixins.entity;

import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    @Shadow
    @Final
    private static TrackedData<ItemStack> STACK;

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;applyWaterBuoyancy()V"))
    void dryClayDustToClayBall(CallbackInfo ci) {
        final var currentStack = getDataTracker().get(STACK);
        if (currentStack.isOf(XenoEarlyStartItemRegistry.DRY_CLAY_DUST.get())) {
            getDataTracker().set(STACK, Items.CLAY_BALL.getDefaultStack().copyWithCount(currentStack.getCount()));
        }
    }
}
