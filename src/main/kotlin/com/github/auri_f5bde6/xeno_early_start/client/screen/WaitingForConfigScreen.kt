package com.github.auri_f5bde6.xeno_early_start.client.screen

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.config.XenoEarlyStartConfig
import com.github.auri_f5bde6.xeno_early_start.config.XenoEarlyStartConfigGui
import com.github.auri_f5bde6.xeno_early_start.network.NetworkHandler
import me.shedaniel.clothconfig2.gui.AbstractConfigScreen
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

class WaitingForConfigScreen(val prevScreen: Screen) :
    Screen(Text.translatable("xeno_early_start.screen.waiting_for_config")) {

    enum class Status {
        READY,
        FAILED,
        PENDING,
        SEND_TO_SERVER,
        QUIT_DIRECTLY
    }

    var tickEclipsed = 0

    var config: XenoEarlyStartConfig.Config? = null
    var status: Status = Status.PENDING
        private set

    var configScreen: AbstractConfigScreen? = null

    fun update(config: XenoEarlyStartConfig.Config) {
        this.config = config
        status = Status.READY
    }

    fun markFailed() {
        status = Status.FAILED
    }

    override fun init() {
        when (status) {
            Status.PENDING -> {
                if (minecraft.currentServerEntry == null) {
                    status = Status.FAILED
                    openConfig()
                } else if ((minecraft.server?.getPermissionLevel(
                        minecraft.player?.gameProfile ?: return
                    ) ?: 0) >= 4
                ) {
                    XenoEarlyStart.LOGGER.info("Player is not OP, or server running in offline mode, skipping config sync")
                    status = Status.FAILED
                    openConfig()
                } else {
                    XenoEarlyStart.LOGGER.info("Requesting log from server")
                    NetworkHandler.clientRequestConfig()
                }
            }

            Status.SEND_TO_SERVER -> {
                NetworkHandler.clientSendNewConfig(this.config!!, configScreen!!.isRequiresRestart)
                minecraft.setScreen(prevScreen)
            }

            else -> {
                illegalStatus("initialising")
            }
        }
    }

    override fun tick() {
        when (status) {
            Status.READY -> {
                XenoEarlyStart.LOGGER.info("Received config from server after ${tickEclipsed / 20.0} seconds")
            }

            Status.FAILED -> {
                XenoEarlyStart.LOGGER.info("Server refused to send config, user are probably not OP")
            }

            Status.PENDING -> {
                if (tickEclipsed > 20 * 30) {
                    XenoEarlyStart.LOGGER.warn("The server did not send config in 30 seconds, skipping sync")
                    status = Status.FAILED
                } else {
                    tickEclipsed++
                    return
                }
            }

            else -> {
                illegalStatus("ticking")
            }
        }
        openConfig()
    }

    private fun illegalStatus(where: String) {
        XenoEarlyStart.LOGGER.error("Illegal status of $status while $where, returning to previous screen")
        minecraft.setScreen(prevScreen)
    }
    fun openConfig() {
        status = when (status) {
            Status.FAILED -> {
                Status.QUIT_DIRECTLY
            }

            Status.READY -> {
                Status.SEND_TO_SERVER
            }

            else -> {
                illegalStatus("opening config")
                return
            }
        }
        configScreen = XenoEarlyStartConfigGui.getConfigBuilder(minecraft, this).setParentScreen(this)
            .build() as AbstractConfigScreen
        minecraft.setScreen(configScreen)
    }
}