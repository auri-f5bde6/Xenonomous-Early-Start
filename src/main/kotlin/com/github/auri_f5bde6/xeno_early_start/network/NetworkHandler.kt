package com.github.auri_f5bde6.xeno_early_start.network

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.config.XenoEarlyStartConfig
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraftforge.network.NetworkRegistry
import net.minecraftforge.network.PacketDistributor
import net.minecraftforge.network.simple.SimpleChannel


object NetworkHandler {
    const val PROTOCOL_VERSION: String = "1.0"
    val NEUTRAL_TIL_FED_CHANNEL: SimpleChannel = NetworkRegistry.newSimpleChannel(
        XenoEarlyStart.of("neutral_til_fed_sync"),
        { PROTOCOL_VERSION },
        { anObject: String? -> PROTOCOL_VERSION == anObject },
        { anObject: String? -> PROTOCOL_VERSION == anObject }
    )

    val CONFIG_CHANNEL: SimpleChannel = NetworkRegistry.newSimpleChannel(
        XenoEarlyStart.of("config_sync"),
        { PROTOCOL_VERSION },
        { anObject: String? -> PROTOCOL_VERSION == anObject },
        { anObject: String? -> PROTOCOL_VERSION == anObject }
    )
    var neutralTilFedPacketId: Int = 0
    var configSyncPacketId: Int = 0

    fun register() {
        // This might not be necessarily, as im pretty sure the clint doesn't need to know but oh well
        NEUTRAL_TIL_FED_CHANNEL.registerMessage(
            neutralTilFedPacketId++,
            NeutralTilFedSyncPacket::class.java,
            NeutralTilFedSyncPacket::encode,
            NeutralTilFedSyncPacket::decode,
            NeutralTilFedSyncPacket::handle
        )

        CONFIG_CHANNEL.registerMessage(
            configSyncPacketId++,
            ConfigSyncPacket::class.java,
            ConfigSyncPacket::encode,
            ConfigSyncPacket::decode,
            ConfigSyncPacket::handle
        )
    }

    fun serverSendConfigSyncPacket(conf: XenoEarlyStartConfig.Config, player: ServerPlayerEntity) {
        CONFIG_CHANNEL.send(
            PacketDistributor.PLAYER.with { player },
            ConfigSyncPacket(ConfigSyncPacket.Type.SERVER_SEND_CONFIG, conf, false)
        )
    }

    fun serverSendFailure(player: ServerPlayerEntity) {
        CONFIG_CHANNEL.send(
            PacketDistributor.PLAYER.with { player },
            ConfigSyncPacket(ConfigSyncPacket.Type.SERVER_SEND_FAILURE, null, false)
        )
    }

    fun clientRequestConfig() {
        CONFIG_CHANNEL.send(
            PacketDistributor.SERVER.noArg(),
            ConfigSyncPacket(ConfigSyncPacket.Type.CLIENT_REQUEST_CONFIG, null, false)
        )
    }

    fun clientSendNewConfig(config: XenoEarlyStartConfig.Config, requireRestart: Boolean) {
        CONFIG_CHANNEL.send(
            PacketDistributor.SERVER.noArg(), ConfigSyncPacket(
                ConfigSyncPacket.Type.CLIENT_SEND_CONFIG,
                config,
                requireRestart
            )
        )
    }
}