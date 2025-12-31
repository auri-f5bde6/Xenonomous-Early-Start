package com.github.auri_f5bde6.xeno_early_start.utils

import net.minecraft.util.function.BooleanBiFunction
import net.minecraft.util.math.Box
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes

object TransUtils {
    fun rotateY(shape: VoxelShape): VoxelShape {
        val rotatedBoxes = shape.boundingBoxes.map { box ->
            Box(
                1 - box.maxZ, box.minY, box.minX,
                1 - box.minZ, box.maxY, box.maxX
            )
        }
        var shapes = VoxelShapes.empty()
        for (box in rotatedBoxes) {
            shapes = VoxelShapes.combine(shapes, VoxelShapes.cuboid(box), BooleanBiFunction.OR)
        }
        return shapes
    }
}