package org.teamvoided.world_foundry.data.gen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.RegistryKeys
import java.util.concurrent.CompletableFuture

class WorldgenProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
    FabricDynamicRegistryProvider(o, r) {
    override fun getName(): String = "world_eater"


    override fun configure(reg: HolderLookup.Provider, e: Entries) {
        e.addAll(reg.getLookupOrThrow(RegistryKeys.DENSITY_FUNCTION))
    }
}