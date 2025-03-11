package dev.dreamers.ptd.services

import de.marcely.bedwars.api.BedwarsAPI
import dev.dreamers.ptd.helpers.StorageHelper
import org.bukkit.Material

class ConfigService private constructor() : StorageHelper("config.yml") {

    companion object {
        private val instance = ConfigService()

        fun init() = instance.init()
        fun reload(): Boolean = instance.reload()

        var TEAMCHEST_SOUND: String = "BLOCK_CHEST_CLOSE"
        var PRIVATECHEST_SOUND: String = "BLOCK_ENDER_CHEST_CLOSE"
        var BLACKLISTED_ITEMS: List<String> = listOf("EXAMPLE_ITEM")
        var DEBUG: Boolean = false
        var UPDATE_CHECK_ENABLED: Boolean = true

        var CONFIG_VERSION: Int = 3
    }

    override fun loadValues() {
        val config = getConfig()

        TEAMCHEST_SOUND = config.getString("Settings.TeamChest-Sound")
        PRIVATECHEST_SOUND = config.getString("Settings.PrivateChest-Sound")
        BLACKLISTED_ITEMS = config.getStringList("Settings.BlackListed-Items").ifEmpty { BLACKLISTED_ITEMS }
        DEBUG = config.getBoolean("Debug-Mode.Enabled")
        UPDATE_CHECK_ENABLED = config.getBoolean("Update-Check.enabled")

        CONFIG_VERSION = config.getInt("config-version")
    }
}
