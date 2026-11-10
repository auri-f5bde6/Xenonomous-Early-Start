package com.github.auri_f5bde6.xeno_early_start.network

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.capabilities.HowledData
import net.minecraft.client.MinecraftClient
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier


object HowledClientHandler {
    fun handle(msg: HowledSyncPacket, ctx: Supplier<NetworkEvent.Context>) {
        val entity = MinecraftClient.getInstance().world!!.getEntityById(msg.data.id)
        if (entity != null) {
            val data = HowledData.get(entity)
            if (data != null) {
                data.copy(msg.data)
                return
            } else {
                XenoEarlyStart.LOGGER.warn("Howled data is null for entity ${entity}!")
            }
        } else {
            XenoEarlyStart.LOGGER.warn("Entity with id not found")
        }

    }
}