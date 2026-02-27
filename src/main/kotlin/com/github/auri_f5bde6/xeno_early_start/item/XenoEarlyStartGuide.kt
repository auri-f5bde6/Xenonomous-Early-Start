package com.github.auri_f5bde6.xeno_early_start.item

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import com.github.auri_f5bde6.xeno_early_start.mixins.client.accessors.AdvancementsScreenAccessor
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class XenoEarlyStartGuide(setting: Settings) : Item(setting) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (world.isClient && user is ClientPlayerEntity) {
            val screen = AdvancementsScreen(user.networkHandler.advancementHandler)
            //screen.selectTab()
            MinecraftClient.getInstance().setScreen(screen)
            for ((a, _) in (screen as AdvancementsScreenAccessor).`xeno_early_start$getTabs`().entries) {
                if (a.id == XenoEarlyStart.of("root")) {
                    screen.selectTab(a)
                }
            }
        }
        return TypedActionResult.success(user.getStackInHand(hand))
    }
}