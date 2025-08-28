package net.hellomouse.xeno_early_start.mixins;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.hellomouse.xeno_early_start.ProgressionModConfig;
import net.hellomouse.xeno_early_start.registries.ProgressionModItemRegistry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends EntityMixin {
    @WrapOperation(method = "initEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;getEquipmentForSlot(Lnet/minecraft/entity/EquipmentSlot;I)Lnet/minecraft/item/Item;"))
    private Item initEquipment(EquipmentSlot equipmentSlot, int equipmentLevel, Operation<Item> original) {
        if (this.random.nextFloat() < ProgressionModConfig.mobChanges.getReplaceEntityCopperArmourProbability()) {
            switch (equipmentSlot) {
                case HEAD:
                    return ProgressionModItemRegistry.COPPER_HELMET.get();
                case CHEST:
                    return ProgressionModItemRegistry.COPPER_CHESTPLATE.get();
                case LEGS:
                    return ProgressionModItemRegistry.COPPER_LEGGINGS.get();
                case FEET:
                    return ProgressionModItemRegistry.COPPER_BOOTS.get();
            }
        }
        return original.call(equipmentSlot, equipmentLevel);
    }

    @Definition(id = "getClampedLocalDifficulty", method = "Lnet/minecraft/world/LocalDifficulty;getClampedLocalDifficulty()F")
    @Expression("0.15 * ?.getClampedLocalDifficulty()")
    @ModifyExpressionValue(method = "initEquipment", at = @At("MIXINEXTRAS:EXPRESSION"))
    private float changeEquipmentProbability(float original) {
        return original + ProgressionModConfig.mobChanges.getFlatAdditiveMobSpawnWithEquipment();
    }

    @Shadow
    public void equipStack(EquipmentSlot equipmentSlot, ItemStack stack) {
    }
}
