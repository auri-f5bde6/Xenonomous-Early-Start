package com.github.auri_f5bde6.xeno_early_start.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket
import net.minecraft.server.world.ServerWorld
import net.minecraft.world.World
import net.minecraft.world.explosion.Explosion
import net.minecraft.world.explosion.ExplosionBehavior
import net.minecraftforge.event.ForgeEventFactory

class CoalDustExplosion : Explosion {
    constructor(
        world: World,
        entity: Entity?,
        damageSource: DamageSource?,
        behavior: ExplosionBehavior?,
        x: Double,
        y: Double,
        z: Double,
        power: Float,
        createFire: Boolean,
        destructionType: DestructionType?
    ) : super(world, entity, damageSource, behavior, x, y, z, power, createFire, destructionType)

    companion object {
        fun createExplosion(
            world: World,
            entity: Entity?,
            damageSource: DamageSource?,
            behavior: ExplosionBehavior?,
            x: Double,
            y: Double,
            z: Double,
            power: Float,
            createFire: Boolean,
            particles: Boolean
        ): Explosion {
            val explosion = CoalDustExplosion(
                world,
                entity,
                damageSource,
                behavior,
                x,
                y,
                z,
                power,
                createFire,
                DestructionType.KEEP
            )
            if (ForgeEventFactory.onExplosionStart(world, explosion)) {
            } else {
                explosion.collectBlocksAndDamageEntities()
                explosion.affectWorld(particles)
            }
            if (world is ServerWorld) {
                if (!explosion.shouldDestroy()) {
                    explosion.clearAffectedBlocks()
                }

                for (serverplayer in world.players) {
                    if (serverplayer.squaredDistanceTo(x, y, z) < 4096.0) {
                        serverplayer.networkHandler
                            .sendPacket(
                                ExplosionS2CPacket(
                                    x,
                                    y,
                                    z,
                                    power,
                                    explosion.affectedBlocks,
                                    explosion.affectedPlayers[serverplayer]
                                )
                            )
                    }
                }
            }
            return explosion
        }
    }
}