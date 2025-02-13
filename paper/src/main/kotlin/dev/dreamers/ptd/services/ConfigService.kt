package dev.dreamers.ptd.services

import dev.dreamers.ptd.helpers.StorageHelper

class ConfigService private constructor() : StorageHelper("config.yml") {

    companion object {
        private val instance = ConfigService()

        fun init() = instance.init()
        fun reload(): Boolean = instance.reload()

        var PRINT_SPLASHSCREEN: Boolean = true
        var UPDATE_CHECKER_ENABLED: Boolean = true
        var CONFIG_VERSION: Int = 1
    }

    override fun loadValues() {
        val config = getConfig()
        PRINT_SPLASHSCREEN = config.getBoolean("Print-SplashScreen")
        UPDATE_CHECKER_ENABLED = config.getBoolean("Update-Checker.enabled")
        CONFIG_VERSION = config.getInt("config-version")
    }
}
