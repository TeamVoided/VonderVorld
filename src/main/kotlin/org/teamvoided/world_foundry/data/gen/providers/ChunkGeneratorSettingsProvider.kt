package org.teamvoided.world_foundry.data.gen.providers

import net.minecraft.block.Blocks
import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.biome.source.util.OverworldBiomeParameters
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings
import net.minecraft.world.gen.chunk.GenerationShapeConfig
import net.minecraft.world.gen.surfacebuilder.VanillaSurfaceRules
import org.teamvoided.world_foundry.data.world.WFChunkGeneratorSettings

object ChunkGeneratorSettingsProvider {
    fun bootstrap(c: BootstrapContext<ChunkGeneratorSettings>) {
        c.register(
            WFChunkGeneratorSettings.AMPLIFIED_MIXTURE,
            createOverworldMixSettings(c)
        )
    }

    private fun createOverworldMixSettings(
        c: BootstrapContext<*>
    ): ChunkGeneratorSettings {
        return ChunkGeneratorSettings(
            GenerationShapeConfig.SURFACE,
            Blocks.STONE.defaultState,
            Blocks.WATER.defaultState,
            DensityFunctionProvider.OverworldNoiseSettingsMakerAmplifiedMixture(
                c.getRegistryLookup(RegistryKeys.DENSITY_FUNCTION),
                c.getRegistryLookup(RegistryKeys.NOISE_PARAMETERS)
            ),
            VanillaSurfaceRules.getOverworldRules(),
            OverworldBiomeParameters().spawnSuitabilityNoises,
            63,
            false,
            true,
            true,
            false
        )
    }
}