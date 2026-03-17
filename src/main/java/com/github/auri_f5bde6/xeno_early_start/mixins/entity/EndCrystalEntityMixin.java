package com.github.auri_f5bde6.xeno_early_start.mixins.entity;

import com.github.auri_f5bde6.xeno_early_start.config.XenoEarlyStartConfig;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndCrystalEntity.class)
public abstract class EndCrystalEntityMixin extends Entity {
    public EndCrystalEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "crystalDestroyed", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/boss/dragon/EnderDragonFight;crystalDestroyed(Lnet/minecraft/entity/decoration/EndCrystalEntity;Lnet/minecraft/entity/damage/DamageSource;)V"))
    void spawnPhantom(DamageSource source, CallbackInfo ci, @Local(type = EnderDragonEntity.class) EnderDragonFight enderDragonFight) {
        var world = getWorld();
        if (world instanceof ServerWorld serverWorld) {
            var shouldSpawnPhantom = world.getBiome(getBlockPos()).matchesKey(BiomeKeys.THE_END) && enderDragonFight.getAliveEndCrystals() > 0;
            if (!shouldSpawnPhantom) {
                return;
            }
            var random = world.random;
            var difficulty = world.getLocalDifficulty(getBlockPos());
            for (int i = 0; i < 4; i++) {
                var pos = getPos().add(random.nextDouble() * 6 - 3, random.nextDouble() * 4, random.nextDouble() * 6 - 3);
                var blockPos = BlockPos.ofFloored(pos);
                var phantom = EntityType.PHANTOM.create(world);
                if (phantom != null) {
                    var size = random.nextInt(XenoEarlyStartConfig.config.mobChanges.getMaxPhantomSize());
                    phantom.refreshPositionAndAngles(blockPos, 0.0F, 0.0F);
                    phantom.initialize(serverWorld, difficulty, SpawnReason.NATURAL, null, null);
                    phantom.setPhantomSize(size);
                    serverWorld.spawnEntityAndPassengers(phantom);
                }
            }
        }
    }
}
