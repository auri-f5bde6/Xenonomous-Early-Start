package net.hellomouse.progression_change.registries;

import net.hellomouse.progression_change.ProgressionMod;
import net.hellomouse.progression_change.block.block_entity.BrickFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ProgressionModBlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> DEF_REG = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ProgressionMod.MODID);
    public static final RegistryObject<BlockEntityType<BrickFurnaceBlockEntity>> BRICK_FURNACE =
            DEF_REG.register("brick_furnace", () ->
                    BlockEntityType.Builder.create(BrickFurnaceBlockEntity::new, ProgressionModBlockRegistry.BRICK_FURNACE.get())
                            .build(null)
            );
}
