package com.github.auri_f5bde6.xeno_early_start.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeBoat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin extends Entity implements IForgeBoat {
    public BoatEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    @Nullable
    public abstract LivingEntity getControllingPassenger();

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;isSilent()Z", shift = At.Shift.BEFORE))
    public void tick(CallbackInfo ci) {
        var entity = getControllingPassenger();
        if (entity instanceof PlayerEntity player) {
            player.getHungerManager().addExhaustion(0.2f / 4f);
        }
    }
}
