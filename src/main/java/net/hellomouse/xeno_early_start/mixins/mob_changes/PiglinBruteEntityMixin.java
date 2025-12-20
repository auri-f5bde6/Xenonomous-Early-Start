package net.hellomouse.xeno_early_start.mixins.mob_changes;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.mob.PiglinBruteEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

@Mixin(PiglinBruteEntity.class)
public abstract class PiglinBruteEntityMixin extends AbstractPiglinEntity {
    public PiglinBruteEntityMixin(EntityType<? extends AbstractPiglinEntity> arg, World arg2) {
        super(arg, arg2);
    }

    @Unique
    @Nullable
    private static Item xeno_early_start$getEquipmentForSlot(EquipmentSlot equipmentSlot, float i) {
        switch (equipmentSlot) {
            case HEAD:
                if (i < 0.05) {
                    return Items.NETHERITE_HELMET;
                } else {
                    return Items.GOLDEN_HELMET;
                }
            case CHEST:
                if (i < 0.05) {
                    return Items.NETHERITE_CHESTPLATE;
                } else {
                    return Items.GOLDEN_CHESTPLATE;
                }
            case LEGS:
                if (i < 0.05) {
                    return Items.NETHERITE_LEGGINGS;
                } else {
                    return Items.GOLDEN_LEGGINGS;
                }
            case FEET:
                if (i < 0.05) {
                    return Items.NETHERITE_BOOTS;
                } else {
                    return Items.GOLDEN_BOOTS;
                }
            default:
                return null;
        }
    }

    @WrapMethod(method = "initEquipment")
    void initEquipment(Random random, LocalDifficulty localDifficulty, Operation<Void> original) {
        original.call(random, localDifficulty);
        if (random.nextFloat() < 0.15F * localDifficulty.getClampedLocalDifficulty()) {
            float f = this.getWorld().getDifficulty() == Difficulty.HARD ? 0.1F : 0.25F;
            boolean flag = true;
            float i = random.nextFloat();
            for (EquipmentSlot equipmentslot : EquipmentSlot.values()) {
                if (equipmentslot.getType() == EquipmentSlot.Type.ARMOR) {
                    ItemStack itemstack = this.getEquippedStack(equipmentslot);
                    if (!flag && random.nextFloat() < f) {
                        break;
                    }

                    flag = false;
                    if (itemstack.isEmpty()) {
                        Item item = xeno_early_start$getEquipmentForSlot(equipmentslot, i);
                        if (item != null) {
                            this.equipStack(equipmentslot, new ItemStack(item));
                        }
                    }
                }
            }
        }
    }
}
