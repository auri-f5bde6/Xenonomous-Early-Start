package net.hellomouse.xeno_early_start.registries;

import net.hellomouse.xeno_early_start.ProgressionMod;
import net.hellomouse.xeno_early_start.entity.BrickEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ProgressionModEntityRegistry {
    public static final DeferredRegister<EntityType<?>> DEF_REG = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ProgressionMod.MODID);
    public static final RegistryObject<EntityType<BrickEntity>> BRICK = DEF_REG.register("brick",
            () -> EntityType.Builder.create((EntityType.EntityFactory<BrickEntity>) BrickEntity::new, SpawnGroup.MISC)
                    .setDimensions(0.5F, 0.5F)
                    .build("brick"));
}
