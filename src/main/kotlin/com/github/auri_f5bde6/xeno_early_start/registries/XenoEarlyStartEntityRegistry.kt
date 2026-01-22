package com.github.auri_f5bde6.xeno_early_start.registries

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.entity.BrickEntity
import com.github.auri_f5bde6.xeno_early_start.entity.ProwlerEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.world.World
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object XenoEarlyStartEntityRegistry {
    val DEF_REG: DeferredRegister<EntityType<*>> =
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, XenoEarlyStart.MODID)

    @JvmField
    val BRICK: RegistryObject<EntityType<BrickEntity>> = DEF_REG.register(
        "brick",
    ) {
        EntityType.Builder.create({ brickEntityEntityType: EntityType<BrickEntity>, world: World ->
            BrickEntity(
                brickEntityEntityType,
                world
            )
        }, SpawnGroup.MISC)
            .setDimensions(0.5f, 0.5f)
            .build("brick")
    }

    @JvmField
    val PROWLER: RegistryObject<EntityType<ProwlerEntity>> = DEF_REG.register("prowler") {
        EntityType.Builder.create({ brickEntityEntityType: EntityType<ProwlerEntity>, world: World ->
            ProwlerEntity(
                brickEntityEntityType,
                world
            )
        }, SpawnGroup.MONSTER).setDimensions(0.6f, 1.7f).maxTrackingRange(8).build("prowler")
    }
}
