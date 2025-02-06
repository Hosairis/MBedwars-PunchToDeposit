package dev.dreamers.ptd.services

import dev.dejvokep.boostedyaml.YamlDocument
import dev.dreamers.ptd.helpers.StorageHelper

class ConfigService private constructor() : StorageHelper("config.yml") {

    companion object {
        var CONFIG_VERSION: Int = 1

        private val instance = ConfigService()

        fun init() = instance.init()
        fun reload() = instance.reload()
    }

    override fun loadValues() {
        val config = getConfig()
        CONFIG_VERSION = config.getInt("config-version", 1)
    }
}
