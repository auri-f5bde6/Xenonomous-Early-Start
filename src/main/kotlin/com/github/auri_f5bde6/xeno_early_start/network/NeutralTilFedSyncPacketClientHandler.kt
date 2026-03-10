package com.github.auri_f5bde6.xeno_early_start.network

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.capabilities.NeutralTilFedData
import net.minecraft.client.MinecraftClient
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier


object NeutralTilFedSyncPacketClientHandler {
    fun handle(msg: NeutralTilFedSyncPacket, ctx: Supplier<NetworkEvent.Context>) {
        val entity = MinecraftClient.getInstance().world!!.getEntityById(msg.data.id)
        if (entity != null) {
            val data = NeutralTilFedData.get(entity)
            if (data != null) {
                data.copy(msg.data)
                return
            } else {
                XenoEarlyStart.LOGGER.warn("Neutral til feed data is null for entity ${entity}!")
            }
        } else {
            XenoEarlyStart.LOGGER.warn("Entity with id not found")
        }

    }
}