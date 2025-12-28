package net.hellomouse.xeno_early_start.mixins.mob_changes.neutral_till_fed;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = AnimalEntity.class)
public abstract class TillFedSharedMixin extends TillFedEntityMixin {

    protected TillFedSharedMixin(EntityType<? extends LivingEntity> arg, World arg2) {
        super(arg, arg2);
    }

    @WrapMethod(method = "interactMob")
    public ActionResult interactMob(PlayerEntity player, Hand hand, Operation<ActionResult> original) {
        var result = original.call(player, hand);
        if (xeno_early_start$hasFeedTrackedData() && result != ActionResult.FAIL && result != ActionResult.PASS && !xeno_early_start$haveBeenFed()) {
            xeno_early_start$setBeenFed(true);
        }
        return result;
    }

}
