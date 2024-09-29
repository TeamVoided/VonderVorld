package org.teamvoided.world_foundry.data.gen.providers

import net.minecraft.util.function.ToFloatFunction
import net.minecraft.util.math.Spline
import kotlin.math.min

object MixinFunctions {

    @JvmStatic
    fun <C, I : ToFloatFunction<C>> oceanFloorSpline(
        ridgesFolded: I,
        pointLowest: Float,
        pointLow: Float,
        pointMiddle: Float,
        pointHigh: Float,
        pointHighest: Float,
        amplifier: ToFloatFunction<Float>
    ): Spline<C, I> {
        val derivative1 = (0.5f * (pointLow - pointLowest)).toDouble().toFloat()
        val derivative2 = 5.0f * (pointMiddle - pointLow)
        return Spline.builder(ridgesFolded, amplifier)
            .add(-1.0f, pointLowest, derivative1)
            .add(-0.75f, pointLow, min(derivative1, derivative2))
            .add(0.0f, pointMiddle, derivative2)
            .add(0.5f, pointHigh, 2.0f * (pointHigh - pointMiddle))
            .add(1.0f, pointHighest, 0.7f * (pointHighest - pointHigh))
            .build()
    }
}
