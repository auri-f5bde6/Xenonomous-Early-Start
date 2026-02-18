package com.github.auri_f5bde6.xeno_early_start.event

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.passive.IronGolemEntity
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.util.math.Box
import net.minecraft.village.VillagerProfession
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@Suppress("UNUSED_PARAMETER")
@EventBusSubscriber(modid = XenoEarlyStart.MODID, bus = EventBusSubscriber.Bus.FORGE)
object LivingAttackEventHandler {
    @SubscribeEvent
    fun onAttackEvent(event: LivingAttackEvent) {
        val attacked = event.entity
        val attacker = event.source.attacker
        if (attacker != null) {
            val f = attacker.world.getLocalDifficulty(attacker.blockPos).localDifficulty
            if ((attacker is LivingEntity && attacker.mainHandStack.isEmpty) || (attacker !is LivingEntity)) {
                if (attacker.isOnFire() && attacker.world.getRandom()
                        .nextFloat() < f * 0.3f
                ) {
                    attacked.setOnFireFor(2 * f.toInt())
                }
            }
            if (attacked is VillagerEntity && attacked.villagerData.profession != VillagerProfession.NITWIT && attacker is LivingEntity) {
                attacked.world.getEntitiesByClass(
                    IronGolemEntity::class.java,
                    Box(attacked.blockPos.add(-50, -50, -50), attacked.blockPos.add(50, 50, 50))
                ) { golemEntity ->
                    golemEntity.attacker = attacker; false
                } // finger crossed it doesn't cause too much lag
            }
        }

    }
}