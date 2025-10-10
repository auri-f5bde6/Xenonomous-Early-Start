package net.hellomouse.xeno_early_start.mixins;

import net.hellomouse.xeno_early_start.ProgressionModConfig;
import net.hellomouse.xeno_early_start.entity.goal.WalkOnRawBrickGoal;
import net.hellomouse.xeno_early_start.registries.ProgressionModItemRegistry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin extends MobEntityMixin {
    @Inject(method = "initEquipment", at = @At("TAIL"))
    protected void maybeSpawnWithCopperSword(Random random, LocalDifficulty localDifficulty, CallbackInfo ci) {
        if (random.nextFloat() < ProgressionModConfig.mobChanges.getEntitySpawnWithCopperToolProbability()) {
            int i = random.nextInt(3);
            if (i == 0) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ProgressionModItemRegistry.COPPER_SWORD.get()));
            } else {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ProgressionModItemRegistry.COPPER_SHOVEL.get()));
            }
        }
    }

    @Inject(method = "initGoals", at = @At("HEAD"))
    void addStepOnRawBrickGoal(CallbackInfo ci) {
        goalSelector.add(5, new WalkOnRawBrickGoal(((ZombieEntity) (Object) this), 0.1, 3));
    }
}