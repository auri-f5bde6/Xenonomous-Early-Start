package com.github.auri_f5bde6.xeno_early_start.mixins.block_changes;

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStartConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.BlockState;
import net.minecraft.block.CoralParentBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import static com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartDamageSource.CORAL;
import static com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartDamageSource.getDamageSource;

@Mixin(CoralParentBlock.class)
public class CoralParentBlockMixin extends AbstractBlockMixin {
    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, Operation<Void> original) {
        super.onEntityCollision(state, world, pos, entity, original);
        if (state.getBlock() instanceof CoralParentBlock && !(entity instanceof WaterCreatureEntity) && entity instanceof LivingEntity le && le.getEquippedStack(EquipmentSlot.FEET).isEmpty()) {
            entity.damage(getDamageSource(CORAL, world.getRegistryManager()), XenoEarlyStartConfig.config.blockChanges.getCoralDamage());
        }
    }
}
