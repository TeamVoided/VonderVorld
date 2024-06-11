package org.teamvoided.world_foundry.data.world

import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings
import org.teamvoided.world_foundry.WorldFoundry.id

object WFChunkGeneratorSettings {
    val ENDWORLD: RegistryKey<ChunkGeneratorSettings> = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, id("endworld"))

}