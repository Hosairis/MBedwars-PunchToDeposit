package dev.dreamers.ptd.services

import org.bukkit.Bukkit

class LogService {
    companion object {
        @JvmStatic
        fun info(input: String) {
            Bukkit.getLogger().info(input)
        }

        @JvmStatic
        fun warning(input: String) {
            Bukkit.getLogger().warning(input)
        }

        @JvmStatic
        fun severe(input: String) {
            Bukkit.getLogger().severe(input)
        }
    }
}
