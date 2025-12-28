package net.hellomouse.xeno_early_start;

import net.minecraft.entity.data.TrackedData;

import javax.annotation.Nullable;

public interface TillFedInterface {
    @Nullable
    TrackedData<Boolean> xeno_early_start$getFeedTrackedData();

    boolean xeno_early_start$hasFeedTrackedData();

    boolean xeno_early_start$haveBeenFed();

    void xeno_early_start$setBeenFed(boolean b);
}
