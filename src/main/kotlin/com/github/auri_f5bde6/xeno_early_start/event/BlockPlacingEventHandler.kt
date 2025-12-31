package com.github.auri_f5bde6.xeno_early_start.event

import com.github.auri_f5bde6.xeno_early_start.ProgressionMod
import com.github.auri_f5bde6.xeno_early_start.mixins.accessors.FlowableFluidAccessor
import net.minecraft.fluid.FlowableFluid
import net.minecraft.fluid.Fluids
import net.minecraft.util.math.Direction
import net.minecraftforge.event.level.BlockEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@Suppress("UNUSED_PARAMETER")
@EventBusSubscriber(modid = ProgressionMod.MODID, bus = EventBusSubscriber.Bus.FORGE)
object BlockPlacingEventHandler {
    @SubscribeEvent
    fun onBlockPlaced(event: BlockEvent.EntityPlaceEvent) {
        val placedBlock = event.placedBlock
        event.entity
        val blockPos = event.pos
        val world = event.level
        val fluidState = event.blockSnapshot.replacedBlock.fluidState
        if (!fluidState.isEmpty) {
            val fluid = fluidState.fluid
            if (fluid is FlowableFluid) {
                if ((fluid as FlowableFluidAccessor).`xeno_early_start$canFlow`(
                        world,
                        blockPos.up(),
                        fluidState.blockState,
                        Direction.DOWN,
                        blockPos,
                        placedBlock,
                        Fluids.EMPTY.defaultState,
                        fluid
                    )
                ) {
                    event.isCanceled = true
                }
            }

        }
    }
}