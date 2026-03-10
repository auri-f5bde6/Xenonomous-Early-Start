package com.github.auri_f5bde6.xeno_early_start.network

import com.github.auri_f5bde6.xeno_early_start.capabilities.NeutralTilFedData
import net.minecraft.network.PacketByteBuf
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier


class NeutralTilFedSyncPacket(val data: NeutralTilFedData) {
    companion object {
        fun encode(msg: NeutralTilFedSyncPacket, buf: PacketByteBuf) {
            buf.writeBoolean(msg.data.fed)
            buf.writeVarInt(msg.data.id)
        }

        fun decode(buf: PacketByteBuf): NeutralTilFedSyncPacket {
            return NeutralTilFedSyncPacket(NeutralTilFedData(buf.readBoolean(), buf.readVarInt(), null))
        }

        fun handleFromClientPacket(msg: NeutralTilFedSyncPacket, ctx: Supplier<NetworkEvent.Context>) {
            /*val player = ctx.get().sender
            if (player!=null){
                val entity = player.world.getEntityById(msg.data.id)
                if (entity!=null){
                    val data = NeutralTilFedData.get(entity)
                    if (data!=null){
                        data.feed()
                        return
                    }else{
                        XenoEarlyStart.LOGGER.warn("Neutral til feed data is null for entity ${entity}!")
                    }
                }else{
                    XenoEarlyStart.LOGGER.warn("Entity with id not found")
                }
            }*/
        }

        fun handle(msg: NeutralTilFedSyncPacket, ctx: Supplier<NetworkEvent.Context>) {
            ctx.get().enqueueWork {
                DistExecutor.unsafeRunWhenOn(
                    Dist.CLIENT
                ) {
                    Runnable { NeutralTilFedSyncPacketClientHandler.handle(msg, ctx) }
                }
            }
            ctx.get().enqueueWork { handleFromClientPacket(msg, ctx) }
            ctx.get().packetHandled = true
        }
    }
}