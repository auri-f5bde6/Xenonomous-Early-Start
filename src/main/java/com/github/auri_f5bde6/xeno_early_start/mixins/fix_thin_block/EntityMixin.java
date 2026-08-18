package com.github.auri_f5bde6.xeno_early_start.mixins.fix_thin_block;

import com.github.auri_f5bde6.xeno_early_start.registries.XenoEarlyStartBlockRegistry;
import com.github.auri_f5bde6.xeno_early_start.utils.OtherUtils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

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
    private final boolean onGround;

    @Shadow
    public abstract Vec3d getPos();


    @Shadow
    public abstract boolean damage(DamageSource source, float amount);

    @WrapOperation(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onSteppedOn(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/Entity;)V"))
    void move(Block instance, World world, BlockPos pos, BlockState state, Entity entity, Operation<Void> original) {
        if (world.getBlockState(pos).isOf(XenoEarlyStartBlockRegistry.RAW_BRICK.get()) || world.getBlockState(pos.offset(Direction.DOWN)).isOf(XenoEarlyStartBlockRegistry.RAW_BRICK.get())) {
            var stepping_on = OtherUtils.raycastSteppingOn(world, getPos(), ((Entity) (Object) this));
            var block = stepping_on.component2().getBlock();
            if (stepping_on.component2().isOf(XenoEarlyStartBlockRegistry.RAW_BRICK.get())) {
                original.call(block, world, stepping_on.component1(), stepping_on.component2(), entity);
            } else {
                original.call(instance, world, pos, state, entity);
            }
        }
    }

    @WrapMethod(method = "isInvulnerableTo")
    protected boolean customInvulnerability(DamageSource damageSource, Operation<Boolean> original) {
        return original.call(damageSource);
    }
}
