package net.hellomouse.xeno_early_start.mixins;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.FoodComponents;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.math.random.Xoroshiro128PlusPlusRandom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(FoodComponents.class)
public class FoodComponentsMixin {
    @Shadow
    public static final FoodComponent BEEF = new FoodComponent.Builder()
            .hunger(3)
            .saturationModifier(0.3F)
            .effect(() -> new StatusEffectInstance(StatusEffects.HUNGER, 600, 0), 0.35f)
            .meat()
            .build();
    @Shadow
    public static final FoodComponent COD = new FoodComponent.Builder()
            .hunger(2)
            .saturationModifier(0.1F)
            .effect(() -> new StatusEffectInstance(StatusEffects.HUNGER, 600, 0), 0.35f)
            .build();
    @Shadow
    public static final FoodComponent MUTTON = new FoodComponent.Builder()
            .hunger(2)
            .saturationModifier(0.3F)
            .effect(() -> new StatusEffectInstance(StatusEffects.POISON, 100, 0), 0.6F)
            .effect(() -> new StatusEffectInstance(StatusEffects.HUNGER, 600, 0), 0.6f)
            .meat()
            .build();
    @Shadow
    public static final FoodComponent TROPICAL_FISH = new FoodComponent.Builder()
            .hunger(1)
            .saturationModifier(0.1F)
            .effect(() -> new StatusEffectInstance(StatusEffects.POISON, 100, 0), 0.6F)
            .effect(() -> new StatusEffectInstance(StatusEffects.HUNGER, 600, 0), 0.6f)
            .build();
    @Shadow
    public static final FoodComponent POTATO = new FoodComponent.Builder()
            .hunger(1)
            .saturationModifier(0.3F)
            .effect(() -> new StatusEffectInstance(StatusEffects.POISON, 100, 0), 0.6F)
            .effect(() -> new StatusEffectInstance(StatusEffects.HUNGER, 600, 0), 0.6f)
            .build();
    @Shadow
    public static final FoodComponent CHICKEN = new FoodComponent.Builder()
            .hunger(2)
            .saturationModifier(0.3F)
            .effect(() -> new StatusEffectInstance(StatusEffects.HUNGER, 600, 1), 1F)
            .effect(() -> new StatusEffectInstance(StatusEffects.POISON, 100, 1), 1F)
            .meat()
            .build();
    @Shadow
    public static final FoodComponent PORKCHOP = new FoodComponent.Builder()
            .hunger(3)
            .saturationModifier(0.3F)
            .effect(() -> new StatusEffectInstance(StatusEffects.HUNGER, 600, 1), 1F)
            .effect(() -> new StatusEffectInstance(StatusEffects.POISON, 100, 1), 1F)
            .meat()
            .build();
    @Shadow
    public static final FoodComponent SPIDER_EYE = new FoodComponent.Builder()
            .hunger(2)
            .saturationModifier(0.8F)
            .effect(() -> new StatusEffectInstance(StatusEffects.POISON, 100, 2), 1F)
            .effect(() -> new StatusEffectInstance(StatusEffects.NAUSEA, 300, 0), 1F)
            .effect(() -> new StatusEffectInstance(StatusEffects.HUNGER, 600, 1), 1F)
            .build();
    @Shadow
    public static final FoodComponent POISONOUS_POTATO = new FoodComponent.Builder()
            .hunger(2)
            .saturationModifier(0.3F)
            .effect(() -> new StatusEffectInstance(StatusEffects.POISON, 100, 2), 1F)
            .effect(() -> new StatusEffectInstance(StatusEffects.NAUSEA, 300, 0), 1F)
            .effect(() -> new StatusEffectInstance(StatusEffects.HUNGER, 600, 1), 1F)
            .build();
    @Shadow
    public static final FoodComponent PUFFERFISH = new FoodComponent.Builder()
            .hunger(1)
            .saturationModifier(0.1F)
            .effect(() -> new StatusEffectInstance(StatusEffects.HUNGER, 600, 2), 1F)
            .effect(() -> new StatusEffectInstance(StatusEffects.NAUSEA, 300, 0), 1F)
            .effect(() -> new StatusEffectInstance(StatusEffects.WEAKNESS, 300, 1), 1F)
            .effect(() -> new StatusEffectInstance(StatusEffects.BLINDNESS, 300, 0), 1F)
            .build();
    @Unique
    private static final Random rand = new Xoroshiro128PlusPlusRandom(1234);
    @Shadow
    public static final FoodComponent ROTTEN_FLESH = new FoodComponent.Builder()
            .hunger(4)
            .saturationModifier(0.1F)
            .effect(() -> new StatusEffectInstance(StatusEffects.HUNGER, 600, 2), 1F)
            .effect(() -> new StatusEffectInstance(StatusEffects.NAUSEA, 300, 0), 1F)
            .effect(() -> new StatusEffectInstance(StatusEffects.WEAKNESS, 300, 1), 1F)
            .effect(() -> {
                if (rand.nextFloat() < 0.2) {
                    return new StatusEffectInstance(StatusEffects.WITHER, 150);
                } else {
                    return new StatusEffectInstance(StatusEffects.POISON, 100, 2);
                }
            }, 1F)
            .meat()
            .build();
}
