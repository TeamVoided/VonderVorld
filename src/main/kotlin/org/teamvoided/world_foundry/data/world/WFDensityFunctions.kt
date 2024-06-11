package org.teamvoided.world_foundry.data.world

import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.gen.DensityFunction
import org.teamvoided.world_foundry.WorldFoundry.id
import org.teamvoided.world_foundry.WorldFoundry.mc

object WFDensityFunctions {
    val EXAMPLE = create("example")
    val AMPLIFIED_TRANSITION = create("example")


    fun create(id: String): RegistryKey<DensityFunction> = RegistryKey.of(RegistryKeys.DENSITY_FUNCTION, id(id))
}