package net.hellomouse.xeno_early_start.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.world.World

class DustCloudEntity(type: EntityType<DustCloudEntity>, world: World) : Entity(type, world) {
    val RADIUS: TrackedData<Float> =
        DataTracker.registerData(DustCloudEntity::class.java, TrackedDataHandlerRegistry.FLOAT)

    override fun initDataTracker() {
        TODO("Not yet implemented")
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound?) {
        TODO("Not yet implemented")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound?) {
        TODO("Not yet implemented")
    }

}