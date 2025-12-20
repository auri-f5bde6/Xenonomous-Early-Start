package net.hellomouse.xeno_early_start.registries

import net.hellomouse.xeno_early_start.ProgressionMod
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.DamageType
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.text.Text


class ProgressionModDamageSource {
    companion object {
        data class XenoDamageSource(val key: RegistryKey<DamageType>, val messageCount: Int)

        @JvmField
        val STONECUTTER =
            XenoDamageSource(RegistryKey.of(RegistryKeys.DAMAGE_TYPE, ProgressionMod.of("stonecutter")), 1)

        @JvmField
        val AMETHYST = XenoDamageSource(RegistryKey.of(RegistryKeys.DAMAGE_TYPE, ProgressionMod.of("amethyst")), 1)

        @JvmField
        val CORAL = XenoDamageSource(RegistryKey.of(RegistryKeys.DAMAGE_TYPE, ProgressionMod.of("coral")), 1)

        @JvmField
        val FURNACE = XenoDamageSource(RegistryKey.of(RegistryKeys.DAMAGE_TYPE, ProgressionMod.of("furnace")), 1)

        @JvmField
        val FURNACE_EXPLOSIONS =
            XenoDamageSource(RegistryKey.of(RegistryKeys.DAMAGE_TYPE, ProgressionMod.of("furnace")), 1)

        @JvmStatic
        fun getDamageSource(source: XenoDamageSource, dynamicRegistryManager: DynamicRegistryManager): DamageSource {
            return DamageSourceRandomMessages(
                dynamicRegistryManager[RegistryKeys.DAMAGE_TYPE].entryOf(source.key),
                source.messageCount
            )
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