package dev.dreamers.ptd.services

import dev.dreamers.ptd.helpers.StorageHelper

class ConfigService private constructor() : StorageHelper("config.yml") {

    companion object {
        private val instance = ConfigService()

        fun init() = instance.init()
        fun reload(): Boolean = instance.reload()

        var CONFIG_VERSION: Int = 1
    }

    override fun loadValues() {
        val config = getConfig()
        CONFIG_VERSION = config.getInt("config-version", 1)
    }
}
