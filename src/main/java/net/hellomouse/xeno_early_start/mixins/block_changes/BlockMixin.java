package net.hellomouse.xeno_early_start.mixins.block_changes;

import net.hellomouse.xeno_early_start.ProgressionModConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StonecutterBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.hellomouse.xeno_early_start.registries.ProgressionModDamageSource.causeStonecutterDamage;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "onSteppedOn", at=@At("HEAD"))
    private void damagedByCertainBlocks(World world, BlockPos pos, BlockState state, Entity entity,CallbackInfo ci){
        if (((Object)this) instanceof StonecutterBlock) {
            entity.damage(causeStonecutterDamage(world.getRegistryManager()), ProgressionModConfig.config.blockChanges.getStonecutterDamage());
        }
    }
}
