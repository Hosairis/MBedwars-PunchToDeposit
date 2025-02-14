package dev.dreamers.ptd.services

import dev.dreamers.ptd.helpers.StorageHelper

class ConfigService private constructor() : StorageHelper("config.yml") {

    companion object {
        private val instance = ConfigService()

        fun init() = instance.init()
        fun reload(): Boolean = instance.reload()

        var BLACKLISTED_ITEMS: List<String> = listOf("EXAMPLE_ITEM")
        var BLACKLISTED_CONTAINERS: List<String> = listOf("EXAMPLE_CONTAINER")
        var UPDATE_CHECK_ENABLED: Boolean = true
        var PRINT_SPLASHSCREEN: Boolean = true

        var CONFIG_VERSION: Int = 1
    }

    override fun loadValues() {
        val config = getConfig()

        BLACKLISTED_ITEMS = config.getStringList("Settings.BlackListed-Items").ifEmpty { BLACKLISTED_ITEMS }
        BLACKLISTED_CONTAINERS = config.getStringList("Settings.BlackListed-Containers").ifEmpty { BLACKLISTED_CONTAINERS }
        UPDATE_CHECK_ENABLED = config.getBoolean("Update-Check.enabled")
        PRINT_SPLASHSCREEN = config.getBoolean("Print-SplashScreen")

        CONFIG_VERSION = config.getInt("config-version")
    }
}
