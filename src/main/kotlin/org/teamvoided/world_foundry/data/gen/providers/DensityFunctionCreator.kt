package org.teamvoided.world_foundry.data.gen.providers

import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.math.noise.InterpolatedNoiseSampler
import net.minecraft.world.gen.DensityFunction
import net.minecraft.world.gen.noise.NoiseRouterData
import org.teamvoided.world_foundry.data.world.WFDensityFunctions.EXAMPLE

object DensityFunctionCreator {
    fun bootstrap(c: BootstrapContext<DensityFunction>) {
        val noiseParams = c.getRegistryLookup(RegistryKeys.NOISE_PARAMETERS)
        val densityFun = c.getRegistryLookup(RegistryKeys.DENSITY_FUNCTION)
        c.register(
            EXAMPLE,
            InterpolatedNoiseSampler.createUnseeded(
                0.25,
                0.125,
                80.0,
                160.0,
                8.0
            )
        )
    }
}