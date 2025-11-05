package net.hellomouse.xeno_early_start.client.particle

import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class CoalDustParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double,
    spriteProvider: SpriteProvider?
) : SpriteBillboardParticle(world, x, y, z) {
    private val spriteProvider: SpriteProvider?

    init {
        this.gravityStrength = 0.2f
        this.velocityMultiplier = 1.0f
        this.spriteProvider = spriteProvider
        this.velocityX = velocityX + (Math.random() * 2.0 - 1.0) * 0.05f
        this.velocityY = velocityY + (Math.random() * 2.0 - 1.0) * 0.05f
        this.velocityZ = velocityZ + (Math.random() * 2.0 - 1.0) * 0.05f
        this.scale = 0.1f * (this.random.nextFloat() * this.random.nextFloat() * 1.0f + 1.3f)
        this.maxAge = (16.0 / (this.random.nextFloat() * 0.8 + 0.2)).toInt() + 2
        this.setSpriteForAge(spriteProvider)
    }

    override fun getType(): ParticleTextureSheet {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE
    }

    override fun tick() {
        super.tick()
        this.setSpriteForAge(this.spriteProvider)
        this.velocityX *= 0.95
        this.velocityY *= 0.9
        this.velocityZ *= 0.95
    }

    @OnlyIn(Dist.CLIENT)
    class Factory(private val spriteProvider: SpriteProvider?) : ParticleFactory<DefaultParticleType?> {
        override fun createParticle(
            arg: DefaultParticleType?,
            arg2: ClientWorld,
            d: Double,
            e: Double,
            f: Double,
            g: Double,
            h: Double,
            i: Double
        ): Particle {
            val snowflakeParticle = CoalDustParticle(arg2, d, e, f, g, h, i, this.spriteProvider)
            snowflakeParticle.setColor(0.923f, 0.964f, 0.999f)
            return snowflakeParticle
        }
    }
}