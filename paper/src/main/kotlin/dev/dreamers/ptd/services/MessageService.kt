package dev.dreamers.ptd.services

import dev.dreamers.ptd.helpers.MessageHelper
import dev.dreamers.ptd.helpers.StorageHelper
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextReplacementConfig

class MessageService private constructor() : StorageHelper("messages.yml") {

    companion object {
        private val instance = MessageService()

        fun init() = instance.init()
        fun reload(): Boolean = instance.reload()
        private fun setPlaceHolders(input: String): Component = instance.setPlaceHolders(input)

        var PREFIX: Component = MessageHelper.colorize("<dark_gray>[<aqua>PTD<dark_gray>] <reset>")
        var TRANSFER_SUCCESS: Component = setPlaceHolders("%prefix <green>✔ <gray>Transferred total of <white>x%amount %item <gray>into %container")
        var UPDATE_FOUND: Component = setPlaceHolders("%prefix <red>⚠ <click:OPEN_URL:https://github.com/Dreamers-Dev/PunchToDeposit/releases><gray>A new update is available. Please <green>Update <gray>to the latest version.</click>")
        var CONFIG_VERSION: Int = 1
    }

    private fun  setPlaceHolders(input: String): Component {
        return MessageHelper.colorize(input)
            .replaceText(
                TextReplacementConfig.builder().matchLiteral("%prefix").replacement(PREFIX).build()
            )
    }

    override fun loadValues() {
        val config = getConfig()
        PREFIX = setPlaceHolders(config.getString("Prefix"))
        TRANSFER_SUCCESS = setPlaceHolders(config.getString("Transfer-Success"))
        UPDATE_FOUND = setPlaceHolders(config.getString("Update-Found"))
        CONFIG_VERSION = config.getInt("config-version")
    }
}
