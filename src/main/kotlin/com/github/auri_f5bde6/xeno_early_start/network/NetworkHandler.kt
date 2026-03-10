package com.github.auri_f5bde6.xeno_early_start.network

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import net.minecraftforge.network.NetworkRegistry
import net.minecraftforge.network.simple.SimpleChannel


object NetworkHandler {
    const val PROTOCOL_VERSION: String = "1.0"
    val ID = XenoEarlyStart.of("sync")

    val CHANNEL: SimpleChannel = NetworkRegistry.newSimpleChannel(
        ID,
        { PROTOCOL_VERSION },
        { anObject: String? -> PROTOCOL_VERSION == anObject },
        { anObject: String? -> PROTOCOL_VERSION == anObject }
    )
    var packetId: Int = 0

    fun register() {
        CHANNEL.registerMessage(
            packetId++,
            NeutralTilFedSyncPacket::class.java,
            NeutralTilFedSyncPacket::encode,
            NeutralTilFedSyncPacket::decode,
            NeutralTilFedSyncPacket::handle
        )
    }
}