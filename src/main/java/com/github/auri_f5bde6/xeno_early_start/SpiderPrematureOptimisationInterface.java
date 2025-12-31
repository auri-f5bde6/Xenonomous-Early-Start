package com.github.auri_f5bde6.xeno_early_start;

import org.spongepowered.asm.mixin.Unique;


public interface SpiderPrematureOptimisationInterface {
    @Unique
    boolean getXeno_early_start$findClosestTargetCalled();

    @Unique
    void setXeno_early_start$findClosestTargetCalled(boolean value);
}
