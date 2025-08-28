package net.hellomouse.xeno_early_start.entity

import net.hellomouse.xeno_early_start.registries.ProgressionModEntityRegistry
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.common.Tags

class BrickEntity : PersistentProjectileEntity {
    var bounced: Boolean = false
    var futureVelocity: Vec3d? = Vec3d.ZERO
    var brickStack: ItemStack = ItemStack(Items.BRICK)


    constructor(world: World?, owner: LivingEntity, stack: ItemStack) : super(
        ProgressionModEntityRegistry.BRICK.get(),
        owner,
        world
    ) {
        this.brickStack = stack.copy()
    }

    constructor(brickEntityEntityType: EntityType<BrickEntity>, world: World?) : super(brickEntityEntityType, world)

    override fun asItemStack(): ItemStack {
        return brickStack.copy()
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        val entity = entityHitResult.entity
        val f = 4.5f

        val owner = this.owner
        val damageSource = this.damageSources.trident(this, if (owner == null) this else owner)
        if (entity.damage(damageSource, f)) {
            if (entity.type === EntityType.ENDERMAN) {
                return
            }

            if (entity is LivingEntity) {
                if (owner is LivingEntity) {
                    EnchantmentHelper.onUserDamaged(entity, owner)
                    EnchantmentHelper.onTargetDamaged(owner, entity)
                }
                this.onHit(entity)
            }
        }
        this.velocity = this.velocity.multiply(-0.005, -0.1, -0.005)
    }

    override fun onBlockHit(blockHitResult: BlockHitResult) {
        val world = getWorld()
        if (!world.isClient()) {
            val blockState = world.getBlockState(blockHitResult.blockPos)
            if (blockState.block.registryEntry.containsTag(Tags.Blocks.GLASS)) {
                world.breakBlock(blockHitResult.blockPos, false, this)
                this.moveBrickAwayFrom(blockHitResult, 0.9f)
                this.velocity = this.velocity.multiply(0.7)
                return
            }
        }
        if (blockHitResult.side != Direction.UP) {
            val directionVector = this.moveBrickAwayFrom(blockHitResult, 0.7f)
            futureVelocity = directionVector.multiply(0.05)
            this.setVelocity(0.0, 0.0, 0.0)
            this.bounced = true
        } else {
            super.onBlockHit(blockHitResult)
            this.setOnGround(true)
        }
    }

    fun moveBrickAwayFrom(blockHitResult: BlockHitResult, blocks: Float): Vec3d {
        // This is going to be very verbose because I have math skill issues, and I am stupid
        val blockPos = blockHitResult.getPos()
        // Direction vector from block pos to entity
        val directionVector = this.pos.subtract(blockPos).normalize()
        // Move the entity away from the block by 0.7
        this.setPosition(blockPos.add(directionVector.multiply(blocks.toDouble())))
        return directionVector
    }

    override fun tick() {
        super.tick()
        if (bounced) {
            this.velocity = futureVelocity
            bounced = false
        }
    }
}
