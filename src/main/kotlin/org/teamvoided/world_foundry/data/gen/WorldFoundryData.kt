package org.teamvoided.world_foundry.data.gen

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistrySetBuilder
import org.teamvoided.world_foundry.WorldFoundry.log
import org.teamvoided.world_foundry.data.gen.providers.ChunkGeneratorSettingsProvider
import org.teamvoided.world_foundry.data.gen.providers.DensityFunctionProvider
import org.teamvoided.world_foundry.data.gen.providers.tags.WorldPresetTagsProvider

class WorldFoundryData : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        log.info("Hello from DataGen")
        val pack = gen.createPack()

        pack.addProvider(::WorldgenProvider)
        pack.addProvider(::WorldPresetTagsProvider)
    }

    override fun buildRegistry(gen: RegistrySetBuilder) {
        gen.add(RegistryKeys.DENSITY_FUNCTION, DensityFunctionProvider::bootstrap)
        gen.add(RegistryKeys.GENERATOR_TYPE, WorldPresetTagsProvider::bootstrap)
        gen.add(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ChunkGeneratorSettingsProvider::bootstrap)
    }
}