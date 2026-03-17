package com.github.auri_f5bde6.xeno_early_start.mixins.entity.arrow_break_glass;

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStartTags;
import com.github.auri_f5bde6.xeno_early_start.entity.BrickEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends ProjectileEntity {
    protected PersistentProjectileEntityMixin(EntityType<? extends ProjectileEntity> arg, World arg2) {
        super(arg, arg2);
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isAir()Z"))
    public boolean tick(BlockState instance, Operation<Boolean> original) {
        return original.call(instance) || instance.isIn(BlockTags.LEAVES) || instance.isIn(Tags.Blocks.GLASS);
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;raycast(Lnet/minecraft/world/RaycastContext;)Lnet/minecraft/util/hit/BlockHitResult;"))
    public BlockHitResult fakeNoCollision(World instance, RaycastContext raycastContext, Operation<BlockHitResult> original) {
        var result = original.call(instance, raycastContext);
        var entity = (PersistentProjectileEntity) (Object) this;
        if (entity instanceof ArrowEntity || entity instanceof TridentEntity || entity instanceof BrickEntity) {
            var blockState = instance.getBlockState(result.getBlockPos());
            var isLeaf = blockState.isIn(XenoEarlyStartTags.Blocks.PROJECTILE_CAN_PASS_THROUGH);
            var isGlass = blockState.isIn(XenoEarlyStartTags.Blocks.BRITTLE);
            if (isGlass || isLeaf) {
                if (isGlass) {
                    instance.breakBlock(result.getBlockPos(), false, entity);
                    entity.setVelocity(entity.getVelocity().multiply(0.7));
                } else {
                    var r = BlockView.raycast(raycastContext.getStart(), raycastContext.getEnd(), null, (_a, hitPos) -> {
                        var hitState = instance.getBlockState(hitPos);
                        if (hitState.isAir() || Objects.equals(Vec3d.of(hitPos), raycastContext.getStart()) || hitState.isIn(XenoEarlyStartTags.Blocks.PROJECTILE_CAN_PASS_THROUGH) || hitState.isIn(XenoEarlyStartTags.Blocks.BRITTLE)) {
                            if (hitState.isIn(XenoEarlyStartTags.Blocks.BRITTLE)) {
                                instance.breakBlock(hitPos, false, entity);
                                entity.setVelocity(entity.getVelocity().multiply(0.7));
                            }
                            return null;
                        } else {
                            return hitPos;
                        }
                    }, (_a) -> null);
                    entity.setVelocity(entity.getVelocity().multiply(0.85));
                    if (r != null) {
                        Vec3d vec3 = raycastContext.getStart().subtract(raycastContext.getEnd());
                        return new BlockHitResult(raycastContext.getEnd(), Direction.getFacing(vec3.x, vec3.y, vec3.z), r, false);
                    }
                }
                entity.velocityDirty = true;
                return BlockHitResult.createMissed(result.getPos(), result.getSide(), result.getBlockPos());
            }

        }
        return result;
    }
}
