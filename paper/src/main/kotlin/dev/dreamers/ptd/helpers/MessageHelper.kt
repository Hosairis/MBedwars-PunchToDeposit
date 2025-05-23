package dev.dreamers.ptd.helpers

import dev.dreamers.ptd.PunchToDeposit
import dev.dreamers.ptd.services.LogService
import net.md_5.bungee.api.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MessageHelper {
    companion object {
        fun colorize(input: String): String {
            return ChatColor.translateAlternateColorCodes('&', input)
        }

        fun sendMessage(player: Player, message: String) {
            player.sendMessage(colorize(message))
        }

        fun sendMessage(player: CommandSender, message: String) {
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
            LogService.info("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=")
            LogService.info("${PunchToDeposit.PLUGIN_NAME} v${PunchToDeposit.PLUGIN_VERSION} Loaded")
            LogService.info("Developed by Hosairis")
            LogService.info("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=")
        }
    }
}
