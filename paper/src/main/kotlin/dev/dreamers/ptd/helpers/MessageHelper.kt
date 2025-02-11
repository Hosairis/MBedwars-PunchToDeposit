package dev.dreamers.ptd.helpers

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.Player

class MessageHelper {
    companion object {
        val miniMessage = MiniMessage.miniMessage()

        fun sendMessage(player: Player, message: Component) {
            player.sendMessage(message)
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

        fun colorize(input: String): Component {
            return miniMessage.deserialize(
                input
                    .replace("&0", "<black>")
                    .replace("&1", "<dark_blue>")
                    .replace("&2", "<dark_green>")
                    .replace("&3", "<dark_aqua>")
                    .replace("&4", "<dark_red>")
                    .replace("&5", "<dark_purple>")
                    .replace("&6", "<gold>")
                    .replace("&7", "<gray>")
                    .replace("&8", "<dark_gray>")
                    .replace("&9", "<blue>")
                    .replace("&a", "<green>")
                    .replace("&b", "<aqua>")
                    .replace("&c", "<red>")
                    .replace("&d", "<light_purple>")
                    .replace("&e", "<yellow>")
                    .replace("&f", "<white>")
                    .replace("&k", "<obfuscated>")
                    .replace("&l", "<bold>")
                    .replace("&m", "<strikethrough>")
                    .replace("&n", "<underlined>")
                    .replace("&o", "<italic>")
                    .replace("&r", "<reset>")
            )
        }
    }
}
