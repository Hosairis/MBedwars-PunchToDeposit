package dev.dreamers.ptd.helpers

import dev.dreamers.ptd.services.ConfigService
import dev.dreamers.ptd.services.LogService
import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Player

class MessageHelper {
    companion object {
        fun colorize(input: String): String {
            return ChatColor.translateAlternateColorCodes('&', input)
        }

        fun sendMessage(player: Player, message: String) {
            player.sendMessage(colorize(message))
        }

        fun formatString(name: String): String {
            val parts = name.split('_')
            val result = StringBuilder(name.length + parts.size - 1)

            for (i in parts.indices) {
                if (i > 0) result.append(' ')
                result.append(parts[i][0]).append(parts[i].substring(1).lowercase())
            }

            return result.toString()
        }

        fun printSplashScreen() {
            if (!ConfigService.PRINT_SPLASHSCREEN) return

            LogService.info("_______________________ ")
            LogService.info("___  __ \\__  __/__  __ \\")
            LogService.info("__  /_/ /_  /  __  / / /")
            LogService.info("_  ____/_  /   _  /_/ / ")
            LogService.info("/_/     /_/    /_____/  ")
            LogService.info("                        ")
        }
    }
}
