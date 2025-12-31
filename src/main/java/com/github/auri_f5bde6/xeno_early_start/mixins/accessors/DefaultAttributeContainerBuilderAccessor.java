package com.github.auri_f5bde6.xeno_early_start.mixins.accessors;

import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(DefaultAttributeContainer.Builder.class)
public interface DefaultAttributeContainerBuilderAccessor {
    @Accessor("instances")
    Map<EntityAttribute, EntityAttributeInstance> xeno_early_start$getInstances();
}
