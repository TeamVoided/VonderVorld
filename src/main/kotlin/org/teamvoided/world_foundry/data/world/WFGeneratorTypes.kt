package org.teamvoided.world_foundry.data.world

import net.minecraft.client.world.GeneratorType
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import org.teamvoided.world_foundry.WorldFoundry.id

object WFGeneratorTypes {
    val MIXED_AMPLIFIED = create("mixed_amplified")
    // (ender) this is my world type ill do things with it later okay
    val ENDIFIED = create("endified")

    fun create(id: String): RegistryKey<GeneratorType> = RegistryKey.of(RegistryKeys.GENERATOR_TYPE, id(id))
}
