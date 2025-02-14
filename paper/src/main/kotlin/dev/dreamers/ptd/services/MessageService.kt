package dev.dreamers.ptd.services

import dev.dreamers.ptd.helpers.MessageHelper
import dev.dreamers.ptd.helpers.StorageHelper

class MessageService private constructor() : StorageHelper("messages.yml") {

    companion object {
        private val instance = MessageService()

        fun init() = instance.init()
        fun reload(): Boolean = instance.reload()

        private fun setPlaceHolders(input: String): String = instance.setPlaceHolders(input)

        var PREFIX: String = MessageHelper.colorize("&7[&bPTD&7]&r")
        var TRANSFER_SUCCESS: String = setPlaceHolders("%prefix &a✔ &7Deposited &fx%amount %item &7into &f%container")
        var ITEM_BLACKLISTED: String = setPlaceHolders("%prefix &c✖ &7That item is &cBlacklisted &7from being deposited")
        var CONTAINER_BLACKLISTED: String = setPlaceHolders("%prefix &c✖ &7That container is &cBlacklisted &7from accepting items as deposits")
        var RELOAD_SUCCESS: String = setPlaceHolders("%prefix &a✔ &7Config & Message files reloaded &aSuccessfully")
        var RELOAD_FAILED: String = setPlaceHolders("%prefix &c✖ &7Config & Message files reload &cFailed")
        var UPDATE_FOUND: String = setPlaceHolders("%prefix &c⚠ &7New update is available. Please &aUpdate &7to the latest version.")
        var INSUFFICIENT_PERMISSIONS: String = setPlaceHolders("%prefix &c✖ &7You lack the required permission: &c%permission")

        var CONFIG_VERSION: Int = 1
    }

    private fun  setPlaceHolders(input: String): String {
        return MessageHelper.colorize(input).replace("%prefix", PREFIX)
    }

    override fun loadValues() {
        val config = getConfig()

        PREFIX = setPlaceHolders(config.getString("Prefix"))
        TRANSFER_SUCCESS = setPlaceHolders(config.getString("Transfer-Success"))
        ITEM_BLACKLISTED = setPlaceHolders(config.getString("Item-BlackListed"))
        CONTAINER_BLACKLISTED = setPlaceHolders(config.getString("Container-BlackListed"))
        RELOAD_SUCCESS = setPlaceHolders(config.getString("Reload-Success"))
        RELOAD_FAILED = setPlaceHolders(config.getString("Reload-Failed"))
        UPDATE_FOUND = setPlaceHolders(config.getString("Update-Found"))
        INSUFFICIENT_PERMISSIONS = setPlaceHolders(config.getString("Insufficient-Permissions"))

        CONFIG_VERSION = config.getInt("config-version")
    }
}
