package net.hellomouse.xeno_early_start.item

import net.hellomouse.xeno_early_start.entity.PrimitiveArrowEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ArrowItem
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class PrimitiveArrowItem(arg: Settings) : ArrowItem(arg) {
    override fun createArrow(world: World, stack: ItemStack, shooter: LivingEntity): PersistentProjectileEntity {
        val arrow = PrimitiveArrowEntity(world, shooter)
        arrow.initFromStack(stack)
        return arrow
    }

    override fun createEntity(world: World, location: Entity, stack: ItemStack): Entity {
        val pos = location.pos
        val entity =
            PrimitiveArrowEntity(world, pos.getX(), pos.getY(), pos.getZ())
        entity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED
        return entity
    }
}