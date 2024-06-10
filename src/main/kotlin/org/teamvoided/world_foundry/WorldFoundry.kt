package org.teamvoided.world_foundry

import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("unused")
object WorldFoundry {
    const val MODID = "world_foundry"

    @JvmField
    val log: Logger = LoggerFactory.getLogger(WorldFoundry::class.simpleName)

    fun commonInit() {
        log.info("Hello from Common")
    }

    fun clientInit() {
        log.info("Hello from Client")
    }

    fun id(path: String) = Identifier(MODID, path)
}
