package com.github.auri_f5bde6.xeno_early_start.entity

import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartEntityRegistry
import com.google.common.collect.HashMultimap
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.CreeperEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import java.util.*

class ProwlerEntity(entityType: EntityType<out ProwlerEntity>, world: World) :
    CreeperEntity(entityType, world) {

    companion object {
        val SPEED_MODIFIER_UUID: UUID = UUID.fromString("21803325-7a32-4916-bbd6-23d9b1d33dcf")

        fun canSpawn(
            type: EntityType<out ProwlerEntity>,
            world: WorldAccess,
            reason: SpawnReason,
            pos: BlockPos,
            random: Random
        ): Boolean {
            return pos.y < 0
        }

        @JvmStatic
        fun createProwlerAttribute(): DefaultAttributeContainer.Builder {
            return createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 24.0)
                .add(EntityAttributes.GENERIC_ARMOR, 4.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
        }
    }

    override fun getType(): EntityType<*> {
        return XenoEarlyStartEntityRegistry.PROWLER.get()
    }

    override fun tickMovement() {
        // Random speed ~ every 1.5 second (30 tick)
        if (random.nextFloat() < (1.0 / 30.0)) {
            val multimap = HashMultimap.create<EntityAttribute, EntityAttributeModifier>()
            multimap.put(
                EntityAttributes.GENERIC_MOVEMENT_SPEED,
                EntityAttributeModifier(
                    SPEED_MODIFIER_UUID,
                    "erratic_prowler",
                    0.8 + (world.random.nextFloat()) * 0.25,
                    EntityAttributeModifier.Operation.MULTIPLY_BASE
                )
            )
            this.attributes.addTemporaryModifiers(multimap)
        }
        super.tickMovement()
    }
}