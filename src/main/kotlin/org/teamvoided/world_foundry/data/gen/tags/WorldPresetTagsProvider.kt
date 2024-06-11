package org.teamvoided.world_foundry.data.gen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.client.world.GeneratorType
import net.minecraft.client.world.GeneratorTypes
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.WorldPresetTags
import java.util.concurrent.CompletableFuture

class WorldPresetTagsProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider<GeneratorType>(o, RegistryKeys.GENERATOR_TYPE, r) {
    override fun configure(arg: HolderLookup.Provider) {
        getOrCreateTagBuilder(WorldPresetTags.NORMAL)
            .add(GeneratorTypes.NORMAL)
    }

    companion object{

    }
}