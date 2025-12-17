package net.hellomouse.xeno_early_start.mixins.block_changes;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.hellomouse.xeno_early_start.ProgressionModConfig;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CoralParentBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import static net.hellomouse.xeno_early_start.registries.ProgressionModDamageSource.CORAL;
import static net.hellomouse.xeno_early_start.registries.ProgressionModDamageSource.getDamageSource;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {

    @WrapMethod(method = "onEntityCollision")
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, Operation<Void> original) {
        if (state.getBlock() instanceof CoralParentBlock && entity instanceof LivingEntity le && le.getEquippedStack(EquipmentSlot.FEET).isEmpty()) {
            entity.damage(getDamageSource(CORAL, world.getRegistryManager()), ProgressionModConfig.config.blockChanges.getCoralDamage());
        }
        original.call(state, world, pos, entity);
    }
}
