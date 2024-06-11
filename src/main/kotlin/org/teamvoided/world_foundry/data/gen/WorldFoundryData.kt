package org.teamvoided.world_foundry.data.gen

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistrySetBuilder
import org.teamvoided.world_foundry.WorldFoundry.log
import org.teamvoided.world_foundry.data.gen.providers.DensityFunctionCreator

class WorldFoundryData : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        log.info("Hello from DataGen")
        val pack = gen.createPack()
//        val pack = gen.createBuiltinResourcePack(id(MODID))


        pack.addProvider(::WorldgenProvider)
    }

    override fun buildRegistry(gen: RegistrySetBuilder) {
        gen.add(RegistryKeys.DENSITY_FUNCTION, DensityFunctionCreator::bootstrap)
    }


//    fun FabricDataGenerator.createBuiltinDataPack(id: Identifier): FabricDataGenerator.Pack {
//        val path: Path = this.output.getPath().resolve("resourcepacks").resolve(id.path)
//        return FabricDataGenerator.Pack(true, id.toString(), FabricDataOutput(modContainer, path, strictValidation))
//    }
}