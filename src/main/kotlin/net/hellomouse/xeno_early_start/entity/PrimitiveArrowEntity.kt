package net.hellomouse.xeno_early_start.entity

import net.hellomouse.xeno_early_start.registries.ProgressionModItemRegistry
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.item.ItemStack
import net.minecraft.world.World


class PrimitiveArrowEntity : ArrowEntity {
    constructor(world: World, x: Double, y: Double, z: Double) : super(world, x, y, z)
    constructor(world: World, livingEntity: LivingEntity) : super(world, livingEntity)

    override fun asItemStack(): ItemStack {
        return ItemStack(ProgressionModItemRegistry.PRIMITIVE_ARROW.get())
    }
}