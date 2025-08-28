package net.hellomouse.xeno_early_start.registries

import net.hellomouse.xeno_early_start.ProgressionMod
import net.hellomouse.xeno_early_start.entity.BrickEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.world.World
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier

object ProgressionModEntityRegistry {
    val DEF_REG: DeferredRegister<EntityType<*>> =
        DeferredRegister.create<EntityType<*>>(ForgeRegistries.ENTITY_TYPES, ProgressionMod.Companion.MODID)

    @JvmField
    val BRICK: RegistryObject<EntityType<BrickEntity>> = DEF_REG.register<EntityType<BrickEntity>>(
        "brick",
        Supplier {
            EntityType.Builder.create<BrickEntity>(EntityType.EntityFactory { brickEntityEntityType: EntityType<BrickEntity>, world: World ->
                BrickEntity(
                    brickEntityEntityType,
                    world
                )
            }, SpawnGroup.MISC)
                .setDimensions(0.5f, 0.5f)
                .build("brick")
        })
}
