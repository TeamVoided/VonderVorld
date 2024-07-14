package org.teamvoided.world_foundry.data.gen.providers

import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.RegistryKey
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler
import org.teamvoided.world_foundry.data.world.WFNoise.AMPLIFIED_TRANSITION_NOISE
import org.teamvoided.world_foundry.data.world.WFNoise.CONTINENTS_NOISE_WF
import org.teamvoided.world_foundry.data.world.WFNoise.EROSION_NOISE_WF
import org.teamvoided.world_foundry.data.world.WFNoise.TEMPERATURE_NOISE_WF
import org.teamvoided.world_foundry.data.world.WFNoise.VEGETATION_NOISE_WF

object NoiseCreator {
    fun bootstrap(c: BootstrapContext<DoublePerlinNoiseSampler.NoiseParameters>) {
        c.register(AMPLIFIED_TRANSITION_NOISE, -9, 2.0, 0.0, 2.0, 1.0, 1.0)
        c.registerBiomeNoises(-1, TEMPERATURE_NOISE_WF, VEGETATION_NOISE_WF, CONTINENTS_NOISE_WF, EROSION_NOISE_WF)
    }

    private fun BootstrapContext<DoublePerlinNoiseSampler.NoiseParameters>.registerBiomeNoises(
        octaveOffset: Int,
        temperature: RegistryKey<DoublePerlinNoiseSampler.NoiseParameters>,
        humidity: RegistryKey<DoublePerlinNoiseSampler.NoiseParameters>,
        continentalness: RegistryKey<DoublePerlinNoiseSampler.NoiseParameters>,
        erosion: RegistryKey<DoublePerlinNoiseSampler.NoiseParameters>
    ) {
        this.register(temperature, -10 + octaveOffset, 1.5, 0.0, 1.0, 0.0, 0.0, 0.0)
        this.register(humidity, -8 + octaveOffset, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0)
        this.register(continentalness, -9 + octaveOffset, 1.0, 1.0, 2.0, 2.0, 2.0, 1.0, 1.0, 1.0, 1.0)
        this.register(erosion, -9 + octaveOffset, 1.0, 1.0, 0.0, 1.0, 1.0)
    }

    private fun BootstrapContext<DoublePerlinNoiseSampler.NoiseParameters>.register(
        key: RegistryKey<DoublePerlinNoiseSampler.NoiseParameters>,
        firstOctave: Int,
        firstAmplitude: Double,
        vararg amplitudes: Double
    ) {
        this.register(key, DoublePerlinNoiseSampler.NoiseParameters(firstOctave, firstAmplitude, *amplitudes))
    }
}