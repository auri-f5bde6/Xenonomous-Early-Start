package com.github.auri_f5bde6.xeno_early_start.mixins.entity.brear_or_open_door;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.ai.pathing.LandPathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraftforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LandPathNodeMaker.class)
public abstract class LandPathNodeMakerMixin extends PathNodeMaker {

    @ModifyReturnValue(method = "getDefaultNodeType", at = @At("RETURN"))
    private PathNodeType breakableGlass(PathNodeType original, BlockView world, int x, int y, int z) {
        if (((this.entity instanceof PolarBearEntity polarBearEntity && polarBearEntity.hasAngerTime()) || this.entity instanceof RavagerEntity) && cachedWorld.getBlockState(new BlockPos(x, y, z)).isIn(Tags.Blocks.GLASS)) {
            return PathNodeType.OPEN;
        }

        return original;
    }
}
