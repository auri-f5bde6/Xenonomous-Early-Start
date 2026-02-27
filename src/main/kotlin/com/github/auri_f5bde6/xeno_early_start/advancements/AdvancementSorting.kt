package com.github.auri_f5bde6.xeno_early_start.advancements

import com.github.auri_f5bde6.xeno_early_start.XenoEarlyStart
import net.minecraft.advancement.Advancement
import java.lang.Integer.signum

object AdvancementSorting {
    @JvmField
    val XENO_EARLY_START = XenoEarlyStart.of("root")

    @JvmField
    val GETTING_STARTED = XenoEarlyStart.of("getting_started")

    @JvmField
    val STONE_AGE = XenoEarlyStart.of("stone_age")

    @JvmField
    val IRON_AGE = XenoEarlyStart.of("iron_age/root")

    val defaultOrder
        get() = ArrayList(listOf(XENO_EARLY_START, GETTING_STARTED, STONE_AGE, IRON_AGE))

    private var orders = defaultOrder

    @JvmStatic
    fun remove(a: Advancement) {
        orders.remove(a.id)
    }

    @JvmStatic
    fun shouldBeAfter(a: Advancement, b: Advancement): Int {
        val aId = a.id
        val bId = b.id
        if (!orders.contains(aId)) {
            orders.add(aId)
        }
        if (!orders.contains(bId)) {
            orders.add(bId)
        }
        val aIndex: Int = orders.indexOf(aId)
        val bIndex: Int = orders.indexOf(bId)
        return signum(aIndex - bIndex)
    }
}