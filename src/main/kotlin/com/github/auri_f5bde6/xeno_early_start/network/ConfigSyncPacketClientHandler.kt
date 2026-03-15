package com.github.auri_f5bde6.xeno_early_start.network

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.client.screen.WaitingForConfigScreen
import net.minecraft.client.MinecraftClient
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier


object ConfigSyncPacketClientHandler {
    fun handle(msg: ConfigSyncPacket, ctx: Supplier<NetworkEvent.Context>) {
        ctx.get().enqueueWork {
            val screen = MinecraftClient.getInstance().currentScreen
            if (screen is WaitingForConfigScreen) {
                when (msg.type) {
                    ConfigSyncPacket.Type.SERVER_SEND_CONFIG -> {
                        screen.update(msg.config!!)
                    }

                    ConfigSyncPacket.Type.SERVER_SEND_FAILURE -> {
                        screen.markFailed()
                    }

                    else -> XenoEarlyStart.LOGGER.error("Client received ${msg.type} packet that's meant to be server bound")
                }
            } else {
                XenoEarlyStart.LOGGER.warn("Received unexpected config sync packet, skipping")
            }
        }
    }
}