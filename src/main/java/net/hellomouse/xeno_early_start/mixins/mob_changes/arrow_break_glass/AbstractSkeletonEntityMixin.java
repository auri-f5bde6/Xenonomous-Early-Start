package net.hellomouse.xeno_early_start.mixins.mob_changes.arrow_break_glass;

import net.hellomouse.xeno_early_start.utils.OtherUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonEntityMixin extends HostileEntity implements RangedAttackMob {

    protected AbstractSkeletonEntityMixin(EntityType<? extends HostileEntity> arg, World arg2) {
        super(arg, arg2);
    }

    @Override
    public boolean canSee(Entity entity) {
        if (entity.getWorld() != this.getWorld()) {
            return false;
        } else {
            Vec3d vec3 = new Vec3d(this.getX(), this.getEyeY(), this.getZ());
            Vec3d vec31 = new Vec3d(entity.getX(), entity.getEyeY(), entity.getZ());
            if (vec31.distanceTo(vec3) > 128.0) {
                return false;
            }
            return OtherUtils.raycast(this.getWorld(), vec3, vec31, blockState -> blockState.isIn(Tags.Blocks.GLASS)).component1();
        }
    }
}
