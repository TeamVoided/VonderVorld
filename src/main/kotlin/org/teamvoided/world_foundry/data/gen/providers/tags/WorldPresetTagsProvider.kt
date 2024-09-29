package org.teamvoided.world_foundry.data.gen.providers.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.client.world.GeneratorType
import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.WorldPresetTags
import net.minecraft.world.biome.source.MultiNoiseBiomeSource
import net.minecraft.world.biome.source.TheEndBiomeSource
import net.minecraft.world.biome.util.MultiNoiseBiomeSourceParameterLists
import net.minecraft.world.dimension.DimensionOptions
import net.minecraft.world.dimension.DimensionTypes
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings
import net.minecraft.world.gen.chunk.NoiseChunkGenerator
import org.teamvoided.world_foundry.data.world.WFChunkGeneratorSettings
import org.teamvoided.world_foundry.data.world.WFGeneratorTypes
import java.util.concurrent.CompletableFuture

class WorldPresetTagsProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider<GeneratorType>(o, RegistryKeys.GENERATOR_TYPE, r) {
    override fun configure(arg: HolderLookup.Provider) {
        getOrCreateTagBuilder(WorldPresetTags.NORMAL)
            .add(WFGeneratorTypes.MIXED_AMPLIFIED)
    }

    companion object {
        fun bootstrap(c: BootstrapContext<GeneratorType>) {
            val dim = c.getRegistryLookup(RegistryKeys.DIMENSION_TYPE)
            val cgs = c.getRegistryLookup(RegistryKeys.CHUNK_GENERATOR_SETTINGS)
            val biome = c.getRegistryLookup(RegistryKeys.BIOME)
            val mnbpsl = c.getRegistryLookup(RegistryKeys.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST)
            c.register(
                WFGeneratorTypes.MIXED_AMPLIFIED,
                GeneratorType(
                    mapOf(
                        DimensionOptions.OVERWORLD to
                                DimensionOptions(
                                    dim.getHolderOrThrow(DimensionTypes.OVERWORLD),
                                    NoiseChunkGenerator(
                                        MultiNoiseBiomeSource.create(
                                            mnbpsl.getHolderOrThrow(MultiNoiseBiomeSourceParameterLists.OVERWORLD),
                                        ),
                                        cgs.getHolderOrThrow(WFChunkGeneratorSettings.AMPLIFIED_MIXTURE)
                                    )
                                ),
                        DimensionOptions.NETHER to DimensionOptions(
                            dim.getHolderOrThrow(DimensionTypes.THE_NETHER),
                            NoiseChunkGenerator(
                                MultiNoiseBiomeSource.create(mnbpsl.getHolderOrThrow(MultiNoiseBiomeSourceParameterLists.NETHER)),
                                cgs.getHolderOrThrow(ChunkGeneratorSettings.NETHER)
                            )
                        ),
                        DimensionOptions.END to DimensionOptions(
                            dim.getHolderOrThrow(DimensionTypes.THE_END),
                            NoiseChunkGenerator(
                                TheEndBiomeSource.create(biome),
                                cgs.getHolderOrThrow(ChunkGeneratorSettings.END)
                            )
                        )
                    )
                )
            )
        }
    }
}
