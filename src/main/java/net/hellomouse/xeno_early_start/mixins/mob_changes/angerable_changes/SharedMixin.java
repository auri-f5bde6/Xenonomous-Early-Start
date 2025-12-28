package net.hellomouse.xeno_early_start.mixins.mob_changes.angerable_changes;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(value = {SquidEntity.class, PigEntity.class})
public abstract class SharedMixin extends MobEntityMixin implements Angerable {
    @Unique
    private static final UniformIntProvider xeno_early_start$ANGER_TIME_RANGE = TimeHelper.betweenSeconds(20, 39);
    @Unique
    private int xeno_early_start$angerTime;
    @Unique
    @Nullable
    private UUID xeno_early_start$angryAt;

    public SharedMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void chooseRandomAngerTime() {
        this.setAngerTime(xeno_early_start$ANGER_TIME_RANGE.get(random));
    }

    @Override
    public int getAngerTime() {
        return this.xeno_early_start$angerTime;
    }

    @Override
    public void setAngerTime(int angerTime) {
        this.xeno_early_start$angerTime = angerTime;
    }

    @Nullable
    @Override
    public UUID getAngryAt() {
        return this.xeno_early_start$angryAt;
    }

    @Override
    public void setAngryAt(@Nullable UUID angryAt) {
        this.xeno_early_start$angryAt = angryAt;
    }

    @Override
    void xeno_early_start$injectTickMovement(CallbackInfo ci) {
        if (!this.getWorld().isClient) {
            this.tickAngerLogic((ServerWorld) this.getWorld(), true);
        }
    }
}
