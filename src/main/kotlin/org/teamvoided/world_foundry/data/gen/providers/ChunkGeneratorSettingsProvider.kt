package org.teamvoided.world_foundry.data.gen.providers

import net.minecraft.block.Blocks
import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.biome.source.util.OverworldBiomeParameters
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings
import net.minecraft.world.gen.chunk.GenerationShapeConfig
import net.minecraft.world.gen.noise.NoiseRouterData
import net.minecraft.world.gen.surfacebuilder.VanillaSurfaceRules
import org.teamvoided.world_foundry.data.world.WFChunkGeneratorSettings

object ChunkGeneratorSettingsProvider {
    fun bootstrap(c: BootstrapContext<ChunkGeneratorSettings>) {
        c.register(
            WFChunkGeneratorSettings.ENDWORLD,
            createOverworldSettings(c, false, false)
        )
    }

    private fun createOverworldSettings(
        context: BootstrapContext<*>,
        amplified: Boolean,
        largeBiomes: Boolean
    ): ChunkGeneratorSettings {
        return ChunkGeneratorSettings(
            GenerationShapeConfig.SURFACE,
            Blocks.STONE.defaultState,
            Blocks.WATER.defaultState,
            NoiseRouterData.overworld(
                context.getRegistryLookup(RegistryKeys.DENSITY_FUNCTION),
                context.getRegistryLookup(RegistryKeys.NOISE_PARAMETERS),
                largeBiomes,
                amplified
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