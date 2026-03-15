package com.github.auri_f5bde6.xeno_early_start.network

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.config.XenoEarlyStartConfig
import com.google.gson.Gson
import net.minecraft.network.PacketByteBuf
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier


class ConfigSyncPacket(val type: Type, val config: XenoEarlyStartConfig.Config?) {
    enum class Type {
        CLIENT_REQUEST_CONFIG,
        CLIENT_SEND_CONFIG,
        SERVER_SEND_CONFIG,
        SERVER_SEND_FAILURE
    }

    companion object {
        fun encode(msg: ConfigSyncPacket, buf: PacketByteBuf) {
            buf.writeEnumConstant(msg.type)
            if (msg.config != null) {
                buf.writeBoolean(true)
                val json = Gson().toJson(msg.config)
                buf.writeVarInt(json.length)
                buf.writeString(json, json.length)
            } else {
                buf.writeBoolean(false)
            }
        }

        fun decode(buf: PacketByteBuf): ConfigSyncPacket {
            var config: XenoEarlyStartConfig.Config? = null
            val request = buf.readEnumConstant(Type::class.java)
            if (buf.readBoolean()) {
                val length = buf.readVarInt()
                config = Gson().fromJson(buf.readString(length), XenoEarlyStartConfig.Config::class.java)
            }
            return ConfigSyncPacket(request, config)
        }

        fun handleFromClientPacket(msg: ConfigSyncPacket, ctx: Supplier<NetworkEvent.Context>) {
            val player = ctx.get().sender
            if (player == null) {
                XenoEarlyStart.LOGGER.error("Config sync packet of type ${msg.type} sender is null")
                return
            }
            // If its not online, player can just fake their username, ive heard
            if (player.server.getPermissionLevel(player.gameProfile) >= 4 && player.server.isOnlineMode) {
                when (msg.type) {
                    Type.CLIENT_REQUEST_CONFIG -> {
                        NetworkHandler.serverSendConfigSyncPacket(XenoEarlyStartConfig.config, player)
                        return
                    }

                    Type.CLIENT_SEND_CONFIG -> {
                        if (msg.config != null) {
                            XenoEarlyStartConfig.config = msg.config
                            return
                        } else {
                            XenoEarlyStart.LOGGER.error("Expecting client to have sent a config, got null")
                        }

                    }

                    else -> {
                        XenoEarlyStart.LOGGER.error("Server received a client bound message of type ${msg.type}")
                    }
                }
            } else {
                XenoEarlyStart.LOGGER.error("Received a ${msg.type} config sync packet received from non-op, ignoring")
            }
            NetworkHandler.serverSendFailure(player)
        }

        fun handle(msg: ConfigSyncPacket, ctx: Supplier<NetworkEvent.Context>) {
            ctx.get().enqueueWork {
                DistExecutor.unsafeRunWhenOn(
                    Dist.CLIENT
                ) {
                    Runnable { ConfigSyncPacketClientHandler.handle(msg, ctx) }
                }
            }
            ctx.get().enqueueWork { handleFromClientPacket(msg, ctx) }
            ctx.get().packetHandled = true
        }
    }
}