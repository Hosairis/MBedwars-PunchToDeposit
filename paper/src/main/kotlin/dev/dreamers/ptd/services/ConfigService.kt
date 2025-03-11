package dev.dreamers.ptd.services

import dev.dreamers.ptd.helpers.MessageHelper
import dev.dreamers.ptd.helpers.StorageHelper

class ConfigService private constructor() : StorageHelper("config.yml") {

    companion object {
        private val instance = ConfigService()

        fun init() = instance.init()
        fun reload(): Boolean = instance.reload()

        var TEAMCHEST_SOUND: String = "BLOCK_CHEST_CLOSE"
        var PRIVATECHEST_SOUND: String = "BLOCK_ENDER_CHEST_CLOSE"

        var HOLOGRAMS_ENABLED: Boolean = true
        var HOLOGRAMS_TEXT: List<String> = listOf("&7PUNCH TO", "&7DEPOSIT")
        var HOLOGRAMS_OFFSET_X: Double = 0.5
        var HOLOGRAMS_OFFSET_Y: Double = 1.0
        var HOLOGRAMS_OFFSET_Z: Double = 0.5

        var BLACKLISTED_ITEMS: List<String> = listOf("WOOD_SWORD", "STONE_SWORD", "IRON_SWORD", "DIAMOND_SWORD", "GOLDEN_SWORD", "BOW", "WOOD_PICKAXE", "WOOD_AXE", "SHEARS")

        var DEBUG: Boolean = false

        var UPDATE_CHECK_ENABLED: Boolean = true

        var CONFIG_VERSION: Int = 3
    }

    override fun loadValues() {
        val config = getConfig()

        TEAMCHEST_SOUND = config.getString("Settings.Sounds.TeamChest")
        PRIVATECHEST_SOUND = config.getString("Settings.Sounds.PrivateChest")

        HOLOGRAMS_ENABLED = config.getBoolean("Settings.holograms.enabled")
        HOLOGRAMS_TEXT = config.getStringList("Settings.holograms.text").map { MessageHelper.colorize(it) }
        HOLOGRAMS_OFFSET_X = config.getDouble("Settings.holograms.offset.x")
        HOLOGRAMS_OFFSET_Y = config.getDouble("Settings.holograms.offset.y")
        HOLOGRAMS_OFFSET_Z = config.getDouble("Settings.holograms.offset.z")

        BLACKLISTED_ITEMS = config.getStringList("Settings.BlackListed-Items")

        DEBUG = config.getBoolean("Debug-Mode.Enabled")

        UPDATE_CHECK_ENABLED = config.getBoolean("Update-Check.Enabled")

        CONFIG_VERSION = config.getInt("config-version")
    }
}
