package net.hellomouse.xeno_early_start.mixins.fix_thin_block;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.hellomouse.xeno_early_start.ProgressionMod;
import net.hellomouse.xeno_early_start.ProgressionModConfig;
import net.hellomouse.xeno_early_start.registries.ProgressionModBlockRegistry;
import net.hellomouse.xeno_early_start.utils.OtherUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    protected final Random random = Random.create();
    @Shadow
    private World world;

    protected EntityMixin(boolean onGround) {
        this.onGround = onGround;
    }

    @Shadow
    public abstract void playSound(SoundEvent sound, float volume, float pitch);

    @Shadow
    private Vec3d pos;

    @Shadow
    public abstract World getWorld();

    @Shadow
    private boolean onGround;

    @Shadow
    public abstract Vec3d getPos();


    @WrapOperation(method = "playStepSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getSoundType(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)Lnet/minecraft/sound/BlockSoundGroup;"))
    BlockSoundGroup fixSmallBlockStepSound(BlockState instance, WorldView worldView, BlockPos blockPos, Entity entity, Operation<BlockSoundGroup> original) {
        var conf = ProgressionModConfig.config.blockChanges.getFixThinBlockStepSound();
        if (conf != ProgressionModConfig.BlockChanges.FixThinBlockStepSound.False) {
            var stepping_on = OtherUtils.raycastSteppingOn(world, this.pos, ((Entity) (Object) this));
            BlockState hitState = stepping_on.component2();
            var ident = ForgeRegistries.BLOCKS.getKey(hitState.getBlock());
            if ((conf == ProgressionModConfig.BlockChanges.FixThinBlockStepSound.True)
                    || (ident != null && Objects.equals(ident.getNamespace(), ProgressionMod.MODID)
                    && conf == ProgressionModConfig.BlockChanges.FixThinBlockStepSound.OnlyThisMod)) {
                return hitState.getSoundType(this.world, stepping_on.component1(), ((Entity) ((Object) this)));
            }
        }
        return original.call(instance, worldView, blockPos, entity);
    }

    @WrapOperation(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onSteppedOn(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/Entity;)V"))
    void move(Block instance, World world, BlockPos pos, BlockState state, Entity entity, Operation<Void> original) {
        var stepping_on = OtherUtils.raycastSteppingOn(world, getPos(), ((Entity) (Object) this));
        var block = stepping_on.component2().getBlock();
        if (stepping_on.component2().isOf(ProgressionModBlockRegistry.RAW_BRICK.get())) {
            original.call(block, world, stepping_on.component1(), stepping_on.component2(), entity);
        } else {
            original.call(instance, world, pos, state, entity);
        }
    }

    @WrapMethod(method = "onPlayerCollision")
    public void batApplyNausea(PlayerEntity player, Operation<Void> original) {
        if ((Entity) (Object) this instanceof BatEntity batEntity) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 1000), batEntity);
        }
        original.call(player);
    }
}
