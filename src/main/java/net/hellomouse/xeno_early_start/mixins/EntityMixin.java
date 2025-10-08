package net.hellomouse.xeno_early_start.mixins;

import net.hellomouse.xeno_early_start.ProgressionMod;
import net.hellomouse.xeno_early_start.ProgressionModConfig;
import net.hellomouse.xeno_early_start.block.BrickBlock;
import net.hellomouse.xeno_early_start.utils.OtherUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.Optional;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    protected final Random random = Random.create();
    @Shadow
    private World world;

    @Shadow
    public abstract void playSound(SoundEvent sound, float volume, float pitch);

    @Shadow
    public float fallDistance;
    @Shadow
    public Optional<BlockPos> supportingBlockPos;
    @Shadow
    private Vec3d pos;

    @Shadow
    public abstract World getWorld();

    @Shadow
    public abstract void onLanding();

    @Inject(method = "playStepSound", at = @At("HEAD"), cancellable = true)
    void fixSmallBlockStepSound(BlockPos pos, BlockState state, CallbackInfo ci) {
        var conf = ProgressionModConfig.blockChanges.getFixThinBlockStepSound();
        if (conf != ProgressionModConfig.BlockChanges.FixThinBlockStepSound.False) {
            var stepping_on = OtherUtils.raycastSteppingOn(world, this.pos, ((Entity) (Object) this));
            BlockHitResult hit = stepping_on.component1();
            BlockState hitState = stepping_on.component2();
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

    @Inject(method = "fall", at = @At("HEAD"), cancellable = true)
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition, CallbackInfo ci) {
        var stepping_on = OtherUtils.raycastSteppingOn(world, this.pos, ((Entity) (Object) this));
        var block = stepping_on.component2().getBlock();
        if (onGround && block instanceof BrickBlock) {
            if (fallDistance > 0.0F) {
                block.onLandedUpon(getWorld(), stepping_on.component2(), stepping_on.component1().getBlockPos(), ((Entity) (Object) this), this.fallDistance);
                this.getWorld()
                        .emitGameEvent(
                                GameEvent.HIT_GROUND,
                                pos,
                                GameEvent.Emitter.of(((Entity) (Object) this), supportingBlockPos.map(arg -> this.getWorld().getBlockState(arg)).orElse(state))
                        );
            }
            onLanding();
            ci.cancel();
        }
    }
}
