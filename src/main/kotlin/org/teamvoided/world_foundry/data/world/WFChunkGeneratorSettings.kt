package org.teamvoided.world_foundry.data.world

import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings
import org.teamvoided.world_foundry.WorldFoundry.id

object WFChunkGeneratorSettings {
    val AMPLIFIED_MIXTURE: RegistryKey<ChunkGeneratorSettings> = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, id("amplified_mixture"))
}
