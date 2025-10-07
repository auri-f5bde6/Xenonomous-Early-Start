package net.hellomouse.xeno_early_start.registries

import net.hellomouse.xeno_early_start.ProgressionMod
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.DamageType
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.text.Text
import net.minecraft.world.event.listener.GameEventListener.Holder


class ProgressionModDamageSource {
    companion object {
        @JvmField
        val STONCUTTER: RegistryKey<DamageType> = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, ProgressionMod.of("stonecutter"))
        @JvmStatic
        fun causeStonecutterDamage(dynamicRegistryManager: DynamicRegistryManager): DamageSource {
            return DamageSourceRandomMessages(dynamicRegistryManager[RegistryKeys.DAMAGE_TYPE].entryOf(STONCUTTER), 1)
        }

        private class DamageSourceRandomMessages : DamageSource {
            private val messageCount: Int

            constructor(message: RegistryEntry<DamageType>, messageCount: Int) : super(message) {
                this.messageCount = messageCount
            }

            constructor(message: RegistryEntry<DamageType>, source: Entity, messageCount: Int) : super(
                message,
                source
            ) {
                this.messageCount = messageCount
            }

            override fun getDeathMessage(attacked: LivingEntity): Text {
                val type = attacked.getRandom().nextInt(this.messageCount)
                val s = "death.damage." + this.name + "_" + type
                val entity: Entity? = this.source ?: this.attacker
                return if (entity != null) {
                    Text.translatable("$s.entity", attacked.displayName, entity.displayName)
                } else {
                    Text.translatable(s, attacked.displayName)
                }
            }
        }
    }
}