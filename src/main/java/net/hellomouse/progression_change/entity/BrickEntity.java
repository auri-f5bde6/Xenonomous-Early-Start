package net.hellomouse.progression_change.entity;

import net.hellomouse.progression_change.registries.ProgressionModEntityRegistry;
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
import net.minecraftforge.common.Tags;

public class BrickEntity extends PersistentProjectileEntity {
    boolean bounced = false;
    Vec3d futureVelocity = Vec3d.ZERO;
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
        var world = getWorld();
        if (!world.isClient()) {
            var blockState = world.getBlockState(blockHitResult.getBlockPos());
            if (blockState.getBlock().getRegistryEntry().containsTag(Tags.Blocks.GLASS)) {
                world.breakBlock(blockHitResult.getBlockPos(), false, this);
                this.moveBrickAwayFrom(blockHitResult, 0.9f);
                this.setVelocity(this.getVelocity().multiply(0.7));
                return;
            }
        }
        if (blockHitResult.getSide() != Direction.UP) {
            var directionVector = this.moveBrickAwayFrom(blockHitResult, 0.7F);
            futureVelocity = directionVector.multiply(0.05);
            this.setVelocity(0, 0, 0);
            this.bounced = true;
        } else {
            super.onBlockHit(blockHitResult);
            this.setOnGround(true);
        }
    }

    public Vec3d moveBrickAwayFrom(BlockHitResult blockHitResult, float blocks) {
        // This is going to be very verbose because I have math skill issues, and I am stupid
        Vec3d blockPos = blockHitResult.getPos();
        // Direction vector from block pos to entity
        Vec3d directionVector = this.getPos().subtract(blockPos).normalize();
        // Move the entity away from the block by 0.7
        this.setPosition(blockPos.add(directionVector.multiply(blocks)));
        return directionVector;
    }

    @Override
    public void tick() {
        super.tick();
        if (bounced) {
            this.setVelocity(futureVelocity);
            bounced = false;
        }
    }
}
