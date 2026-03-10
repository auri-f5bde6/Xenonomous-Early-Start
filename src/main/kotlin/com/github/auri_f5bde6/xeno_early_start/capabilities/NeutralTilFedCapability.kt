package com.github.auri_f5bde6.xeno_early_start.capabilities

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.network.NetworkHandler
import com.github.auri_f5bde6.xeno_early_start.network.NeutralTilFedSyncPacket
import net.minecraft.entity.Entity
import net.minecraft.entity.passive.ChickenEntity
import net.minecraft.entity.passive.CowEntity
import net.minecraft.entity.passive.PigEntity
import net.minecraft.entity.passive.SheepEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.*
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.network.PacketDistributor
import java.util.*

@AutoRegisterCapability
class NeutralTilFedData(hasBeenFed: Boolean, val id: Int, val uuid: UUID?) : ICapabilitySerializable<NbtCompound> {


    var fed = hasBeenFed
        private set

    @EventBusSubscriber(modid = XenoEarlyStart.MODID, bus = EventBusSubscriber.Bus.FORGE)
    companion object {
        val ID = XenoEarlyStart.of("neutral_til_fed")
        val NEUTRAL_TIL_FED_CAPABILITY: Capability<NeutralTilFedData> =
            CapabilityManager.get(object : CapabilityToken<NeutralTilFedData>() {})

        @SubscribeEvent
        fun addToEntity(event: AttachCapabilitiesEvent<Entity>) {
            val entity = event.`object`
            if (entity is ChickenEntity || entity is PigEntity || entity is SheepEntity || entity is CowEntity) {
                event.addCapability(ID, NeutralTilFedData(false, entity.id, entity.uuid))
            }
        }

        @SubscribeEvent
        fun track(event: PlayerEvent.StartTracking) {

        }

        @JvmStatic
        fun get(entity: Entity): NeutralTilFedData? {
            val i = entity.getCapability(NEUTRAL_TIL_FED_CAPABILITY)
            return if (i.isPresent) {
                i.orElseThrow { Exception() }
            } else {
                null
            }
        }
    }

    fun feed(world: World) {
        if (!fed) {
            fed = true
            if (world is ServerWorld) {
                NetworkHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), NeutralTilFedSyncPacket(this))
            }
        }
    }

    fun copy(other: NeutralTilFedData) {
        fed = other.fed
    }

    override fun serializeNBT(): NbtCompound {
        val nbt = NbtCompound()
        nbt.putBoolean("hasBeenFed", fed)
        return nbt
    }

    override fun deserializeNBT(arg: NbtCompound) {
        fed = arg.getBoolean("hasBeenFed")
    }

    override fun <T : Any> getCapability(
        capability: Capability<T>,
        arg: Direction?
    ): LazyOptional<T?> {
        if (capability == NEUTRAL_TIL_FED_CAPABILITY) {
            return LazyOptional.of { this as T }
        }
        return LazyOptional.empty()
    }

}