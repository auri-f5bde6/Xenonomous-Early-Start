package com.github.auri_f5bde6.xeno_early_start.mixins.block_changes;

import com.github.auri_f5bde6.xeno_early_start.ProgressionModConfig;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import static com.github.auri_f5bde6.xeno_early_start.registries.ProgressionModDamageSource.CORAL;
import static com.github.auri_f5bde6.xeno_early_start.registries.ProgressionModDamageSource.getDamageSource;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {

    @WrapMethod(method = "onEntityCollision")
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, Operation<Void> original) {
        if (state.getBlock() instanceof CoralParentBlock && !(entity instanceof WaterCreatureEntity) && entity instanceof LivingEntity le && le.getEquippedStack(EquipmentSlot.FEET).isEmpty()) {
            entity.damage(getDamageSource(CORAL, world.getRegistryManager()), ProgressionModConfig.config.blockChanges.getCoralDamage());
        }
        original.call(state, world, pos, entity);
    }

    @Unique
    private static int xeno_early_start$getBlastFireTickDelay(Random random) {
        return 30 + random.nextInt(10);
    }

    @WrapMethod(method = "scheduledTick")
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random, Operation<Void> original) {
        original.call(state, world, pos, random);
        if (state.isOf(Blocks.BLAST_FURNACE) && state.get(BlastFurnaceBlock.LIT) && ProgressionModConfig.config.blockChanges.getBlastFurnaceSetNearbyBlockOnFire()) {
            world.scheduleBlockTick(pos, (BlastFurnaceBlock) (Object) this, xeno_early_start$getBlastFireTickDelay(world.random));
            // Copied off vanilla lava
            if (world.getGameRules().getBoolean(GameRules.DO_FIRE_TICK)) {
                int i = random.nextInt(3);
                if (i > 0) {
                    BlockPos blockpos = pos;

                    for (int j = 0; j < i; ++j) {
                        blockpos = blockpos.add(random.nextInt(3) - 1, 1, random.nextInt(3) - 1);
                        if (!world.canSetBlock(blockpos)) {
                            return;
                        }

                        BlockState blockstate = world.getBlockState(blockpos);
                        if (blockstate.isAir()) {
                            if (this.xeno_early_start$canLightFire(world, blockpos)) {
                                world.setBlockState(blockpos, AbstractFireBlock.getState(world, blockpos));
                                return;
                            }
                        } else if (blockstate.blocksMovement()) {
                            return;
                        }
                    }
                } else {
                    for (int k = 0; k < 3; ++k) {
                        BlockPos blockpos1 = pos.add(random.nextInt(3) - 1, 0, random.nextInt(3) - 1);
                        if (!world.canSetBlock(blockpos1)) {
                            return;
                        }

                        if (world.isAir(blockpos1.up()) && this.xeno_early_start$isFlammable(world, blockpos1, Direction.UP)) {
                            world.setBlockState(blockpos1.up(), AbstractFireBlock.getState(world, blockpos1));
                        }
                    }
                }
            }
        }
    }

    // Copied off vanilla lava
    @Unique
    private boolean xeno_early_start$canLightFire(WorldView world, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (this.xeno_early_start$isFlammable(world, pos.offset(direction), direction.getOpposite())) {
                return true;
            }
        }

        return false;
    }

    // Copied off vanilla lava
    @Unique
    private boolean xeno_early_start$isFlammable(WorldView level, BlockPos pos, Direction face) {
        return (pos.getY() < level.getBottomY() || pos.getY() >= level.getTopY() || level.isChunkLoaded(pos)) && level.getBlockState(pos).isFlammable(level, pos, face);
    }

    @WrapMethod(method = "onBlockAdded")
    @Deprecated
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, Operation<Void> original) {
        original.call(state, world, pos, oldState, notify);
        if (state.isOf(Blocks.BLAST_FURNACE)) {
            world.scheduleBlockTick(pos, (BlastFurnaceBlock) (Object) this, xeno_early_start$getBlastFireTickDelay(world.random));
        }
    }
}
