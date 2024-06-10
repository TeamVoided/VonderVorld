package org.teamvoided.template

import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("unused")
object Template {
    const val MODID = "template"

    @JvmField
    val log: Logger = LoggerFactory.getLogger(Template::class.simpleName)

    fun commonInit() {
        log.info("Hello from Common")
    }

    fun clientInit() {
        log.info("Hello from Client")
    }
        //UNMAPPED will break later
    fun id(path: String) = Identifier.method_60655(MODID, path)
}
