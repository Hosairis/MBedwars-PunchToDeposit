package dev.dreamers.ptd.helpers

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

class HookHelper {
    companion object {
        var usesMBedwars = false

        fun init() {
            checkMBedwars()
        }

        private fun checkMBedwars() {
            val plugin: Plugin? = Bukkit.getPluginManager().getPlugin("MBedwars")

            if (plugin == null || !plugin.isEnabled) return
            usesMBedwars = true
        }
    }
}
