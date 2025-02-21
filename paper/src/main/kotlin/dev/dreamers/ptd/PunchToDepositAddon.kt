package dev.dreamers.ptd

import de.marcely.bedwars.api.BedwarsAddon
import dev.dreamers.ptd.listeners.InteractListener
import dev.dreamers.ptd.listeners.JoinListener

class PunchToDepositAddon constructor(plugin: PunchToDeposit): BedwarsAddon(plugin) {
    fun registerEvents() {
        plugin.server.pluginManager.registerEvents(InteractListener(), plugin)
        plugin.server.pluginManager.registerEvents(JoinListener(), plugin)
    }
}
