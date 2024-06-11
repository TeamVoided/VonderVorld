package org.teamvoided.world_foundry.data.tags

import net.minecraft.client.world.GeneratorType
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object WFWorldPresetTags {
    val EXAMPLE = create("example")
    val AMPLIFIED_TRANSITION = create("example")

    fun create(id: String): TagKey<GeneratorType> {
        return TagKey.of(RegistryKeys.GENERATOR_TYPE, Identifier(id))
    }
}