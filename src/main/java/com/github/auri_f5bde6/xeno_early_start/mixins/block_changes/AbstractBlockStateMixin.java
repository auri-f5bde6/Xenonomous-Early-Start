package com.github.auri_f5bde6.xeno_early_start.mixins.block_changes;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
    @Shadow
    public abstract boolean isIn(TagKey<Block> tag);

    @ModifyReturnValue(method = "isToolRequired()Z", at = @At("RETURN"))
    public boolean logRequireTool(boolean original) {
        return this.isIn(BlockTags.LOGS) || original;
    }
}
