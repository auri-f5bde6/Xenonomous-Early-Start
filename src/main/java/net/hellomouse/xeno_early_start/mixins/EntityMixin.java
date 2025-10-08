package net.hellomouse.xeno_early_start.mixins;

import net.hellomouse.xeno_early_start.ProgressionMod;
import net.hellomouse.xeno_early_start.ProgressionModConfig;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    protected final Random random = Random.create();

    @Shadow
    public abstract BlockPos getBlockPos();

    @Shadow
    private World world;

    @Shadow
    public abstract void playSound(SoundEvent sound, float volume, float pitch);

    @Inject(method = "playStepSound", at = @At("HEAD"), cancellable = true)
    void fixSmallBlockStepSound(BlockPos pos, BlockState state, CallbackInfo ci) {
        var conf = ProgressionModConfig.blockChanges.getFixThinBlockStepSound();
        if (conf != ProgressionModConfig.BlockChanges.FixThinBlockStepSound.False) {
            var entityPos = getBlockPos();
            RaycastContext context = new RaycastContext(entityPos.toCenterPos(), entityPos.add(0, -1, 0).toCenterPos(), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, (Entity) (Object) this);
            BlockHitResult hit = world.raycast(context);
            BlockState hitState = world.getBlockState(hit.getBlockPos());
            var ident = ForgeRegistries.BLOCKS.getKey(hitState.getBlock());
            if ((conf == ProgressionModConfig.BlockChanges.FixThinBlockStepSound.True)
                    || (ident != null && Objects.equals(ident.getNamespace(), ProgressionMod.MODID)
                    && conf == ProgressionModConfig.BlockChanges.FixThinBlockStepSound.OnlyThisMod)) {
                BlockSoundGroup sound = hitState.getSoundType(this.world, hit.getBlockPos(), ((Entity) ((Object) this)));
                playSound(sound.getStepSound(), sound.getVolume() * 0.15F, sound.getPitch());
                ci.cancel();
            }
        }
    }
}
