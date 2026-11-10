package com.github.auri_f5bde6.xeno_early_start.capabilities

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.network.HowledSyncPacket
import com.github.auri_f5bde6.xeno_early_start.network.NetworkHandler
import net.minecraft.entity.Entity
import net.minecraft.entity.passive.WolfEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.*
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.network.PacketDistributor
import java.util.*

@AutoRegisterCapability
class HowledData(lastHowled: Long, val id: Int, val uuid: UUID?) : ICapabilitySerializable<NbtCompound> {


    var lastHowled = lastHowled
        private set

    @EventBusSubscriber(modid = XenoEarlyStart.MODID, bus = EventBusSubscriber.Bus.FORGE)
    companion object {
        val ID = XenoEarlyStart.of("howled")
        val HOWLED_CAPABILITY: Capability<HowledData> =
            CapabilityManager.get(object : CapabilityToken<HowledData>() {})

        @SubscribeEvent
        fun thing(event: AttachCapabilitiesEvent<Entity>) {
            val entity = event.`object`
            if (entity is WolfEntity) {
                event.addCapability(ID, HowledData(-1, entity.id, entity.uuid))
            }
        }

        @JvmStatic
        fun get(entity: Entity): HowledData? {
            val i = entity.getCapability(HOWLED_CAPABILITY)
            return if (i.isPresent) {
                i.orElseThrow { Exception() }
            } else {
                null
            }
        }
    }

    fun maybeHowl(world: World, day: Long): Boolean {
        if (lastHowled != day) {
            lastHowled = day
            if (world is ServerWorld) {
                NetworkHandler.HOWL_CHANNEL.send(
                    PacketDistributor.ALL.noArg(),
                    HowledSyncPacket(this)
                )
            }
            return true
        } else {
            return false
        }
    }

    override fun serializeNBT(): NbtCompound {
        val nbt = NbtCompound()
        nbt.putLong("lastHowled", lastHowled)
        return nbt
    }

    override fun deserializeNBT(arg: NbtCompound) {
        lastHowled = arg.getLong("lastHowled")
    }

    fun copy(other: HowledData) {
        lastHowled = other.lastHowled
    }

    override fun <T : Any> getCapability(
        capability: Capability<T>,
        arg: Direction?
    ): LazyOptional<T?> {
        if (capability == HOWLED_CAPABILITY) {
            return LazyOptional.of { this as T }
        }
        return LazyOptional.empty()
    }

}