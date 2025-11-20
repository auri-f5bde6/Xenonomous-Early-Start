package net.hellomouse.xeno_early_start.mixins.mob_changes.arrow_break_glass;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;

import static net.hellomouse.xeno_early_start.utils.OtherUtils.moveEntityAwayFrom;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends ProjectileEntity {
    protected PersistentProjectileEntityMixin(EntityType<? extends ProjectileEntity> arg, World arg2) {
        super(arg, arg2);
    }

    @WrapMethod(method = "onBlockHit")
    public void onBlockHit(BlockHitResult blockHitResult, Operation<Void> original) {
        if ((Object) this instanceof ArrowEntity) {
            var world = getWorld();
            var blockState = world.getBlockState(blockHitResult.getBlockPos());
            if (blockState.isIn(Tags.Blocks.GLASS)) {
                world.breakBlock(blockHitResult.getBlockPos(), false, (ArrowEntity) (Object) (this));
                moveEntityAwayFrom((ArrowEntity) (Object) (this), blockHitResult.getPos(), 0.9f);
                this.setVelocity(this.getVelocity().multiply(0.7));
                return;
            }
        }
        original.call(blockHitResult);
    }
}
