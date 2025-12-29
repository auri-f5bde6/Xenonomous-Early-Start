package net.hellomouse.xeno_early_start.mixins.mob_changes;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.hellomouse.xeno_early_start.mixins.accessors.DefaultAttributeContainerBuilderAccessor;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.FishEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Predicate;

@Mixin(FishEntity.class)
public class FishEntityMixin {
    @WrapMethod(method = "createFishAttributes")
    private static DefaultAttributeContainer.Builder createFishAttributes(Operation<DefaultAttributeContainer.Builder> original) {
        var o = original.call();
        o.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, ((DefaultAttributeContainerBuilderAccessor) o).xeno_early_start$getInstances().get(EntityAttributes.GENERIC_MOVEMENT_SPEED).getBaseValue() * 1.5);
        return o;
    }

    @WrapOperation(method = "initGoals", at = @At(value = "NEW", target = "(Lnet/minecraft/entity/mob/PathAwareEntity;Ljava/lang/Class;FDDLjava/util/function/Predicate;)Lnet/minecraft/entity/ai/goal/FleeEntityGoal;"))
    FleeEntityGoal initGoals(PathAwareEntity fleeingEntity, Class classToFleeFrom, float fleeDistance, double fleeSlowSpeed, double fleeFastSpeed, Predicate inclusionSelector, Operation<FleeEntityGoal> original) {
        return new FleeEntityGoal(fleeingEntity, classToFleeFrom, fleeDistance, fleeSlowSpeed * 2, fleeFastSpeed * 2, inclusionSelector);
    }
}
