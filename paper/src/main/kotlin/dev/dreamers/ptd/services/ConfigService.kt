package dev.dreamers.ptd.services

import de.marcely.bedwars.tools.Helper
import dev.dreamers.ptd.helpers.MessageHelper
import dev.dreamers.ptd.helpers.StorageHelper
import org.bukkit.Material

class ConfigService private constructor() : StorageHelper("config.yml") {

    companion object {
        private val instance = ConfigService()

        fun init() = instance.init()
        fun reload(): Boolean = instance.reload()

        var TEAMCHEST_SOUND: String = "BLOCK_CHEST_CLOSE"
        var PRIVATECHEST_SOUND: String = "BLOCK_ENDER_CHEST_CLOSE"

        var HOLOGRAMS_ENABLED: Boolean = true
        var HOLOGRAMS_REMOVE_AFTER_DEPOSIT: Boolean = true
        var HOLOGRAMS_TEXT: List<String> = listOf("&7PUNCH TO", "&7DEPOSIT")
        var HOLOGRAMS_X_SEARCH: Int = 8
        var HOLOGRAMS_Y_SEARCH: Int = 3
        var HOLOGRAMS_Z_SEARCH: Int = 8
        var HOLOGRAMS_OFFSET_X: Double = 0.5
        var HOLOGRAMS_OFFSET_Y: Double = 1.0
        var HOLOGRAMS_OFFSET_Z: Double = 0.5

        var BLACKLISTED_ITEMS: List<Material> = listOf()

        var DEBUG: Boolean = false

        var UPDATE_CHECK_ENABLED: Boolean = true

        var CONFIG_VERSION: Int = 3
    }

    override fun loadValues() {
        val config = getConfig()

        TEAMCHEST_SOUND = config.getString("Settings.Sounds.TeamChest")
        PRIVATECHEST_SOUND = config.getString("Settings.Sounds.PrivateChest")

        HOLOGRAMS_ENABLED = config.getBoolean("Settings.Holograms.Enabled")
        HOLOGRAMS_REMOVE_AFTER_DEPOSIT = config.getBoolean("Settings.Holograms.Remove-After-Deposit")
        HOLOGRAMS_TEXT = config.getStringList("Settings.Holograms.Text").map { MessageHelper.colorize(it) }
        HOLOGRAMS_X_SEARCH = config.getInt("Settings.Holograms.Search.X")
        HOLOGRAMS_Y_SEARCH = config.getInt("Settings.Holograms.Search.Y")
        HOLOGRAMS_Z_SEARCH = config.getInt("Settings.Holograms.Search.Z")
        HOLOGRAMS_OFFSET_X = config.getDouble("Settings.Holograms.Offsets.X")
        HOLOGRAMS_OFFSET_Y = config.getDouble("Settings.Holograms.Offsets.Y")
        HOLOGRAMS_OFFSET_Z = config.getDouble("Settings.Holograms.Offsets.Z")

        BLACKLISTED_ITEMS = config.getStringList("Settings.BlackListed-Items").mapNotNull { Helper.get().getMaterialByName(it) }

        DEBUG = config.getBoolean("Debug-Mode.Enabled")

        UPDATE_CHECK_ENABLED = config.getBoolean("Update-Check.Enabled")

        CONFIG_VERSION = config.getInt("config-version")
    }
}
