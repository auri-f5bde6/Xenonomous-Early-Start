package com.github.auri_f5bde6.xeno_early_start.entity

import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartItemRegistry
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.item.ItemStack
import net.minecraft.world.World


class PrimitiveArrowEntity : ArrowEntity {
    constructor(world: World, x: Double, y: Double, z: Double) : super(world, x, y, z)
    constructor(world: World, livingEntity: LivingEntity) : super(world, livingEntity)

    override fun asItemStack(): ItemStack {
        return ItemStack(XenoEarlyStartItemRegistry.PRIMITIVE_ARROW.get())
    }
}