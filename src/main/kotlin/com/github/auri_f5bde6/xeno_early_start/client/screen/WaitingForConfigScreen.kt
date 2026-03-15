package com.github.auri_f5bde6.xeno_early_start.client.screen

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.config.XenoEarlyStartConfig
import com.github.auri_f5bde6.xeno_early_start.config.XenoEarlyStartConfigGui
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

class WaitingForConfigScreen(val prevScreen: Screen) :
    Screen(Text.translatable("xeno_early_start.screen.waiting_for_config")) {

    var tickEclipsed = 0

    override fun tick() {
        if (minecraft.currentServerEntry == null || (minecraft.server?.getPermissionLevel(
                minecraft.player?.gameProfile ?: return
            ) ?: 0) >= 4
        ) {
            XenoEarlyStart.LOGGER.info("Player is not OP, skipping config sync")
            XenoEarlyStartConfig.serverConfig = XenoEarlyStartConfig.ServerConfig.FAILED()
        }
        when (XenoEarlyStartConfig.serverConfig) {
            is XenoEarlyStartConfig.ServerConfig.READY -> {
                XenoEarlyStart.LOGGER.info("Received config from server after ${tickEclipsed / 20} seconds")
            }

            is XenoEarlyStartConfig.ServerConfig.FAILED -> {
                XenoEarlyStart.LOGGER.info("Server refused to send config")
            }

            is XenoEarlyStartConfig.ServerConfig.PENDING -> {
                if (tickEclipsed > 20 * 30) {
                    XenoEarlyStart.LOGGER.warn("The server did not send config in 30 seconds, skipping sync")
                } else {
                    tickEclipsed++
                    return
                }
            }
        }
        openConfig()
    }

    fun openConfig() {
        minecraft.setScreen(XenoEarlyStartConfigGui.getConfigBuilder(minecraft, prevScreen).build())
    }
}