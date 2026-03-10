package com.github.auri_f5bde6.xeno_early_start.mixins.mob_changes.neutral_till_fed;

import com.github.auri_f5bde6.xeno_early_start.capabilities.NeutralTilFedData;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = AnimalEntity.class)
public abstract class TillFedSharedMixin extends PassiveEntity {
    protected TillFedSharedMixin(EntityType<? extends PassiveEntity> arg, World arg2) {
        super(arg, arg2);
    }

    @WrapMethod(method = "interactMob")
    public ActionResult interactMob(PlayerEntity player, Hand hand, Operation<ActionResult> original) {
        var result = original.call(player, hand);
        var data = NeutralTilFedData.get(((AnimalEntity) (Object) this));
        if (data != null && result != ActionResult.FAIL && result != ActionResult.PASS) {
            data.feed(player.getWorld());
        }
        return result;
    }
}
