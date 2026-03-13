package com.github.auri_f5bde6.xeno_early_start.entity.goal

import com.github.auri_f5bde6.xeno_early_start.capabilities.NeutralTilFedData
import net.minecraft.entity.ai.goal.FleeEntityGoal
import net.minecraft.entity.mob.Angerable
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.entity.player.PlayerEntity
import java.util.function.Supplier

class RunawayFromPlayerGoal(mob: PathAwareEntity, val speed: Double, val beenFed: Supplier<Boolean>) :
    FleeEntityGoal<PlayerEntity>(
        mob,
        PlayerEntity::class.java,
        { true },
        32F,
        speed * 0.8,
        speed,
        { player -> (NeutralTilFedData.get(mob)?.fed != true) && ((mob is Angerable && mob.angryAt != player.uuid) || mob !is Angerable) })