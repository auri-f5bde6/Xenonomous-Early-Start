package com.github.auri_f5bde6.xeno_early_start.mixins;

import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ToolMaterials.class)
public abstract class ToolMaterialsMixin implements ToolMaterial {

    @Shadow
    @Final
    private int miningLevel;

    @Override
    public int getMiningLevel() {
        // Unnecessary because of tier sorting registry, but just in case other mod doesn't use that
        if ((Object) (this) == ToolMaterials.GOLD) {
            return MiningLevels.IRON;
        } else {
            return this.miningLevel;
        }
    }

}
