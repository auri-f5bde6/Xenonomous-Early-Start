package net.hellomouse.progression_change.block.block_entity;

import net.hellomouse.progression_change.client.screen.BrickFurnaceScreenHandler;
import net.hellomouse.progression_change.registries.ProgressionModBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class BrickFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    public BrickFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ProgressionModBlockEntityRegistry.BRICK_FURNACE.get(), pos, state, RecipeType.SMELTING);
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("container.progression_change.brick_furnace");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new BrickFurnaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    public void onOpen(PlayerEntity player) {
        super.onOpen(player);
        System.out.println("HELLO");
    }
}
