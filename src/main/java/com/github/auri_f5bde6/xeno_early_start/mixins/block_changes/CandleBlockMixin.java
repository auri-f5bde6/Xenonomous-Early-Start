package com.github.auri_f5bde6.xeno_early_start.mixins.block_changes;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.BlockState;
import net.minecraft.block.CandleBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CandleBlock.class)
public abstract class CandleBlockMixin extends AbstractBlockMixin {
    @Shadow
    @Final
    public static BooleanProperty LIT;

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, Operation<Void> original) {
        super.onEntityCollision(state, world, pos, entity, original);
        if (state.get(LIT) && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity) entity)) {
            entity.setOnFireFor(1);
        }
    }
}
