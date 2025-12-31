package com.github.auri_f5bde6.xeno_early_start.mixins.block_changes.coal_dust;

import com.github.auri_f5bde6.xeno_early_start.CoalDust;
import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
    @Shadow
    protected ServerWorld world;

    @Inject(method = "continueMining", at = @At("HEAD"))
    private void continueMining(BlockState state, BlockPos pos, int failedStartMiningTime, CallbackInfoReturnable<Float> cir) {
        if (state.isIn(Tags.Blocks.ORES_COAL)) {
            CoalDust.mining(this.world, state, pos);
        }
    }
}
