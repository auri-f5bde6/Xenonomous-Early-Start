package com.github.auri_f5bde6.xeno_early_start.network

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.config.XenoEarlyStartConfig
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier


object ConfigSyncPacketClientHandler {
    fun handle(msg: ConfigSyncPacket, ctx: Supplier<NetworkEvent.Context>) {
        ctx.get().enqueueWork {
            when (msg.type) {
                ConfigSyncPacket.Type.SERVER_SEND_CONFIG -> {
                    XenoEarlyStart.LOGGER.info("Received config from server")
                    XenoEarlyStartConfig.serverConfig = XenoEarlyStartConfig.ServerConfig.READY(msg.config!!)
                }

                ConfigSyncPacket.Type.SERVER_SEND_FAILURE -> {
                    XenoEarlyStart.LOGGER.info("Server refused to sync config, user is probably not op")
                    XenoEarlyStartConfig.serverConfig = XenoEarlyStartConfig.ServerConfig.FAILED()
                }

                else -> XenoEarlyStart.LOGGER.error("Client received packet thats meant to be server bound")
            }
        }
    }
}