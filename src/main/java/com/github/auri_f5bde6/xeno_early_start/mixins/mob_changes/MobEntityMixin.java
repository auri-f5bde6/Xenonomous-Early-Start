package com.github.auri_f5bde6.xeno_early_start.mixins.mob_changes;

import com.github.auri_f5bde6.xeno_early_start.ProgressionModConfig;
import com.github.auri_f5bde6.xeno_early_start.mixins.fix_thin_block.EntityMixin;
import com.github.auri_f5bde6.xeno_early_start.registries.ProgressionModItemRegistry;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends EntityMixin {
    @Shadow
    @Final
    public GoalSelector goalSelector;

    protected MobEntityMixin(boolean onGround) {
        super(onGround);
    }

    @WrapOperation(method = "initEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;getEquipmentForSlot(Lnet/minecraft/entity/EquipmentSlot;I)Lnet/minecraft/item/Item;"))
    private Item initEquipment(EquipmentSlot equipmentSlot, int equipmentLevel, Operation<Item> original) {
        if (this.random.nextFloat() < ProgressionModConfig.config.mobChanges.getReplaceEntityCopperArmourProbability()) {
            // Not using a switch as that cause weird issue related to whatever a "switch map" is
            if (equipmentSlot == EquipmentSlot.HEAD) {
                return ProgressionModItemRegistry.COPPER_HELMET.get();
            } else if (equipmentSlot == EquipmentSlot.CHEST) {
                return ProgressionModItemRegistry.COPPER_CHESTPLATE.get();
            } else if (equipmentSlot == EquipmentSlot.LEGS) {
                return ProgressionModItemRegistry.COPPER_LEGGINGS.get();
            } else if (equipmentSlot == EquipmentSlot.FEET) {
                return ProgressionModItemRegistry.COPPER_BOOTS.get();
            }
        }
        return original.call(equipmentSlot, equipmentLevel);
    }

    @Definition(id = "getClampedLocalDifficulty", method = "Lnet/minecraft/world/LocalDifficulty;getClampedLocalDifficulty()F")
    @Expression("0.15 * ?.getClampedLocalDifficulty()")
    @ModifyExpressionValue(method = "initEquipment", at = @At("MIXINEXTRAS:EXPRESSION"))
    private float changeEquipmentProbability(float original) {
        return original + ProgressionModConfig.config.mobChanges.getFlatAdditiveMobSpawnWithEquipment();
    }

    @Shadow
    public void equipStack(EquipmentSlot equipmentSlot, ItemStack stack) {
    }

}
