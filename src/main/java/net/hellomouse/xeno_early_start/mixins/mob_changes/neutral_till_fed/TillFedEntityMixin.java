package net.hellomouse.xeno_early_start.mixins.mob_changes.neutral_till_fed;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.hellomouse.xeno_early_start.TillFedInterface;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Targeter;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

@Mixin(MobEntity.class)
public abstract class TillFedEntityMixin extends LivingEntity implements Targeter, TillFedInterface {

    @Shadow
    @Final
    public GoalSelector targetSelector;

    @Shadow
    @Final
    public GoalSelector goalSelector;

    protected TillFedEntityMixin(EntityType<? extends LivingEntity> arg, World arg2) {
        super(arg, arg2);
    }

    @Nullable
    @Unique
    public TrackedData<Boolean> xeno_early_start$getFeedTrackedData() {
        return null;
    }

    @Unique
    public boolean xeno_early_start$hasFeedTrackedData() {
        return xeno_early_start$getFeedTrackedData() != null;
    }

    @WrapMethod(method = "initDataTracker")
    void initDataTracker(Operation<Void> original) {
        original.call();
        if (xeno_early_start$hasFeedTrackedData()) {
            dataTracker.startTracking(xeno_early_start$getFeedTrackedData(), false);
        }
    }

    @Unique
    public boolean xeno_early_start$haveBeenFed() {
        return dataTracker.get(xeno_early_start$getFeedTrackedData());
    }

    @Unique
    public void xeno_early_start$setBeenFed(boolean b) {
        dataTracker.set(xeno_early_start$getFeedTrackedData(), b);
    }

    @WrapMethod(method = "writeCustomDataToNbt")
    public void writeCustomDataToNbt(NbtCompound nbt, Operation<Void> original) {
        if (xeno_early_start$hasFeedTrackedData()) {

            nbt.putBoolean("xeno_early_start$isFed", getDataTracker().get(xeno_early_start$getFeedTrackedData()));
        }
        original.call(nbt);
    }

    @WrapMethod(method = "readCustomDataFromNbt")
    public void readCustomDataFromNbt(NbtCompound nbt, Operation<Void> original) {
        if (xeno_early_start$hasFeedTrackedData()) {
            getDataTracker().set(xeno_early_start$getFeedTrackedData(), nbt.getBoolean("xeno_early_start$isFed"));
        }
        original.call(nbt);
    }
}
