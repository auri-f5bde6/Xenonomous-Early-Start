package com.github.auri_f5bde6.xeno_early_start.entity

import com.github.auri_f5bde6.xeno_early_start.block.BrickBlock
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartBlockRegistry
import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartEntityRegistry
import com.github.auri_f5bde6.xeno_early_start.utils.OtherUtils.moveEntityAwayFrom
import net.minecraft.block.Block.NOTIFY_ALL
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.EntityType
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundCategory
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.common.Tags

class BrickEntity : PersistentProjectileEntity {
    var bounced: Boolean = false
    var futureVelocity: Vec3d = Vec3d.ZERO
    var brickStack: ItemStack = ItemStack(Items.BRICK)


    constructor(world: World, owner: LivingEntity, stack: ItemStack) : super(
        XenoEarlyStartEntityRegistry.BRICK.get(),
        owner,
        world
    ) {
        this.brickStack = stack.copy()
    }

    constructor(brickEntityEntityType: EntityType<BrickEntity>, world: World) : super(brickEntityEntityType, world)

    override fun asItemStack(): ItemStack {
        return brickStack.copy()
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        val entity = entityHitResult.entity
        val f = 4.5f

        val owner = this.owner
        val damageSource = this.damageSources.trident(this, owner ?: this)
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
        val blockState = world.getBlockState(blockHitResult.blockPos)
        if (blockState.isIn(Tags.Blocks.GLASS) && velocity.lengthSquared() > 1.5) {
            world.breakBlock(blockHitResult.blockPos, false, this)
            moveEntityAwayFrom(this, blockHitResult.pos, 0.9f)
            this.velocity = this.velocity.multiply(0.7)
            return
        } else {
            if (blockHitResult.side != Direction.UP) {
                val directionVector = moveEntityAwayFrom(this, blockHitResult.pos, 0.9f)
                futureVelocity = directionVector.multiply(0.05)
                this.setVelocity(0.0, 0.0, 0.0)
                this.bounced = true
            } else {
                val above = blockHitResult.blockPos.add(0, 1, 0)
                val block = XenoEarlyStartBlockRegistry.BRICK.get().defaultState.with(
                    BrickBlock.AXIS, horizontalFacing.rotateClockwise(
                        Direction.Axis.Y
                    ).axis
                )
                if (world.getBlockState(above).isAir && block.canPlaceAt(world, above)) {
                    world.setBlockState(
                        above,
                        block,
                        NOTIFY_ALL
                    )
                    world.playSound(
                        pos.x,
                        pos.y,
                        pos.z,
                        BlockSoundGroup.DEEPSLATE_BRICKS.hitSound,
                        SoundCategory.BLOCKS,
                        1.0f,
                        1.0f,
                        true
                    )
                } else {
                    world.spawnEntity(ItemEntity(world, pos.x, pos.y, pos.z, brickStack))
                }
                kill()

            }
        }


    }


    override fun tick() {
        super.tick()
        if (bounced) {
            this.velocity = futureVelocity
            bounced = false
        }
    }
}
