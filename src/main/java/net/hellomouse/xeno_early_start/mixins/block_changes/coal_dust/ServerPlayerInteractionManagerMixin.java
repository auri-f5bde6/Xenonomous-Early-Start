package net.hellomouse.xeno_early_start.mixins.block_changes.coal_dust;

import net.hellomouse.xeno_early_start.registries.XenoProgressionModParticleRegistry;
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
            for (int i = 0; i < world.random.nextBetween(5, 10); i++) {
                this.world.spawnParticles(XenoProgressionModParticleRegistry.COAL_DUST.get(), pos.getX() - 0.1 + world.random.nextFloat() * 1.1, pos.getY() - 0.1 + world.random.nextFloat() * 1.1, pos.getZ() - 0.1 + world.random.nextFloat() * 1.1, 1, 0, -0.01d, 0, 0.005);
            }
        }
    }
}
