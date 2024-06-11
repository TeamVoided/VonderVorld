package org.teamvoided.world_foundry.data.gen.world

import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.gen.DensityFunction
import org.teamvoided.world_foundry.WorldFoundry.id
import org.teamvoided.world_foundry.WorldFoundry.mc

object WFDensityFunctions {
    val EXAMPLE = create("example")
    val TEST = vanilla("test")


    fun create(id: String): RegistryKey<DensityFunction> = RegistryKey.of(RegistryKeys.DENSITY_FUNCTION, id(id))
    fun vanilla(id: String): RegistryKey<DensityFunction> = RegistryKey.of(RegistryKeys.DENSITY_FUNCTION, mc(id))
}