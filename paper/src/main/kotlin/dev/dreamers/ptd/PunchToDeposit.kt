package dev.dreamers.ptd

import dev.dreamers.ptd.helpers.HookHelper
import dev.dreamers.ptd.listeners.InteractListener
import dev.dreamers.ptd.helpers.UpdateHelper
import dev.dreamers.ptd.listeners.JoinListener
import dev.dreamers.ptd.services.ConfigService
import dev.dreamers.ptd.services.MessageService
import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class PunchToDeposit : JavaPlugin() {
    companion object {
        private lateinit var plugin: PunchToDeposit

        fun getInst(): PunchToDeposit {
            return plugin
        }
    }

    override fun onEnable() {
        plugin = this
        ConfigService.init()
        MessageService.init()

        Metrics(this, 24460)
        HookHelper.init()
        Bukkit.getPluginManager().registerEvents(InteractListener(), this)
        Bukkit.getPluginManager().registerEvents(JoinListener(), this)

        UpdateHelper.startUpdateCheck()
    }

    override fun onDisable() {}
}
