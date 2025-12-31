package com.github.auri_f5bde6.xeno_early_start.mixins.mob_changes.arrow_break_glass;

import com.github.auri_f5bde6.xeno_early_start.utils.OtherUtils;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Definition(id = "raycast", method = "Lnet/minecraft/world/World;raycast(Lnet/minecraft/world/RaycastContext;)Lnet/minecraft/util/hit/BlockHitResult;")
    @Definition(id = "getWorld", method = "Lnet/minecraft/entity/LivingEntity;getWorld()Lnet/minecraft/world/World;")
    @Expression("this.getWorld().raycast(?).?()")
    @WrapOperation(method = "canSee", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    HitResult.Type canSeePastGlass(BlockHitResult instance, Operation<HitResult.Type> original, @Local(ordinal = 0) Vec3d vec3, @Local(ordinal = 1) Vec3d vec31) {
        var entity = (LivingEntity) (Object) this;
        if (entity instanceof AbstractSkeletonEntity
                || entity instanceof PillagerEntity
                || (entity instanceof PiglinEntity piglinEntity && piglinEntity.getMainHandStack().isIn(Tags.Items.TOOLS_CROSSBOWS))
                || (entity instanceof DrownedEntity drownedEntity && drownedEntity.getMainHandStack().isIn(Tags.Items.TOOLS_TRIDENTS))) {
            if (OtherUtils.raycast(this.getWorld(), vec3, vec31, blockState -> blockState.isIn(Tags.Blocks.GLASS) || blockState.isIn(BlockTags.LEAVES)).component1()) {
                return HitResult.Type.MISS;
            } else {
                return HitResult.Type.BLOCK;
            }
        }
        return original.call(instance);
    }
}
