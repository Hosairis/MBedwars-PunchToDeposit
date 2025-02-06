package dev.dreamers.ptd.services

import dev.dreamers.ptd.helpers.StorageHelper

class MessageService private constructor() : StorageHelper("config.yml") {

    companion object {
        var CONFIG_VERSION: Int = 1

        private val instance = MessageService()

        fun init() = instance.init()
        fun reload() = instance.reload()
    }

    override fun loadValues() {
        val config = getConfig()
        CONFIG_VERSION = config.getInt("config-version", 1)
    }
}
