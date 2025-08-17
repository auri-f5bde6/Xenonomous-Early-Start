package net.hellomouse.progression_change.entity;

import net.hellomouse.progression_change.ProgressionModEntityRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BrickEntity extends PersistentProjectileEntity {
    ItemStack brickStack = new ItemStack(Items.BRICK);


    public BrickEntity(World world, LivingEntity owner, ItemStack stack) {
        super(ProgressionModEntityRegistry.BRICK.get(), owner, world);
        this.brickStack = stack.copy();
    }

    public BrickEntity(EntityType<BrickEntity> brickEntityEntityType, World world) {
        super(brickEntityEntityType, world);
    }

    @Override
    protected ItemStack asItemStack() {
        return brickStack.copy();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        float f = 4.5f;

        Entity owner = this.getOwner();
        DamageSource damageSource = this.getDamageSources().trident(this, owner == null ? this : owner);
        if (entity.damage(damageSource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity livingEntity2) {
                if (owner instanceof LivingEntity) {
                    EnchantmentHelper.onUserDamaged(livingEntity2, owner);
                    EnchantmentHelper.onTargetDamaged((LivingEntity) owner, livingEntity2);
                }
                this.onHit(livingEntity2);
            }
        }
        this.setVelocity(this.getVelocity().multiply(-0.005, -0.1, -0.005));
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if (blockHitResult.getSide() != Direction.UP) {
            super.onBlockHit(blockHitResult);
            Vec3d vec3 = blockHitResult.getPos().subtract(this.getX(), this.getY(), this.getZ());
            this.setVelocity(vec3);
            Vec3d vec31 = vec3.normalize().multiply(0.5F);
            this.setPos(this.getX() - vec31.x, this.getY() - vec31.y, this.getZ() - vec31.z);
        } else {
            super.onBlockHit(blockHitResult);
            this.setOnGround(true);
        }
    }

}
