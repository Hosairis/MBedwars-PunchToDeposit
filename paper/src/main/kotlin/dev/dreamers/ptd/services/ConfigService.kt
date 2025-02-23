package dev.dreamers.ptd.services

import de.marcely.bedwars.api.BedwarsAPI
import dev.dreamers.ptd.helpers.StorageHelper
import org.bukkit.Material

class ConfigService private constructor() : StorageHelper("config.yml") {

    companion object {
        private val instance = ConfigService()

        fun init() = instance.init()
        fun reload(): Boolean = instance.reload()

        var TEAMCHEST_ENABLED: Boolean = false
        var TEAMCHEST_BLOCK: Material = Material.CHEST
        var INTERACTING: Boolean = false
        var BLACKLISTED_ITEMS: List<String> = listOf("EXAMPLE_ITEM")
        var UPDATE_CHECK_ENABLED: Boolean = true

        var CONFIG_VERSION: Int = 1
    }

    override fun loadValues() {
        val config = getConfig()

        TEAMCHEST_ENABLED = BedwarsAPI.getConfigurationAPI().getValue("teamchest-enabled") as Boolean
        TEAMCHEST_BLOCK = BedwarsAPI.getConfigurationAPI().getValue("teamchest-block") as Material
        INTERACTING = BedwarsAPI.getConfigurationAPI().getValue("interacting") as Boolean
        BLACKLISTED_ITEMS = config.getStringList("Settings.BlackListed-Items").ifEmpty { BLACKLISTED_ITEMS }
        UPDATE_CHECK_ENABLED = config.getBoolean("Update-Check.enabled")

        CONFIG_VERSION = config.getInt("config-version")
    }
}
