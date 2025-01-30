package example.plugin.template

import org.bukkit.plugin.java.JavaPlugin

class Template: JavaPlugin() {

    override fun onEnable() {
        // Called when the plugin is enabled (initialize resources, register events/commands).
    }

    override fun onDisable() {
        // Called when the plugin is disabled (cleanup resources, save data).
    }
}