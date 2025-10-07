package net.hellomouse.xeno_early_start.mixins;

import net.hellomouse.xeno_early_start.utils.OtherUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.block.CampfireBlock.LIT;
import static net.minecraft.block.CampfireBlock.extinguish;

@Mixin(CampfireBlockEntity.class)
public class CampfireBlockEntityMixin {
    @Inject(method = "litServerTick", at=@At(value = "HEAD"), cancellable = true)
    private static void extinguishWhenRaining(World world, BlockPos pos, BlockState state, CampfireBlockEntity campfire, CallbackInfo ci) {
        if (OtherUtils.canSeeSky(world, pos)&&(world.isRaining()||world.isThundering())) {
            if (!world.isClient()) {
                world.playSound((PlayerEntity)null, pos, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            extinguish(null, world, pos,state);
            world.setBlockState(pos, state.with(LIT, false), Block.NOTIFY_ALL);
            ci.cancel();
        }
    }
}
