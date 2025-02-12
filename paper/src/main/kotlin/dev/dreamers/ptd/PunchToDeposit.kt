package dev.dreamers.ptd

import dev.dreamers.ptd.helpers.HookHelper
import dev.dreamers.ptd.helpers.MessageHelper
import dev.dreamers.ptd.listeners.InteractListener
import dev.dreamers.ptd.helpers.UpdateHelper
import dev.dreamers.ptd.listeners.JoinListener
import dev.dreamers.ptd.services.ConfigService
import dev.dreamers.ptd.services.ConsoleColors
import dev.dreamers.ptd.services.LogService
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
        try {
            plugin = this

            ConfigService.init()
            MessageService.init()

            MessageHelper.printSplashScreen()
            LogService.logColored("Loaded Config", ConsoleColors.GREEN)
            LogService.logColored("Loaded Messages", ConsoleColors.GREEN)

            HookHelper.init()
            if (HookHelper.usesMBedwars) LogService.logColored("Hooked into MBedwars", ConsoleColors.GREEN)

            Bukkit.getPluginManager().registerEvents(InteractListener(), this)
            Bukkit.getPluginManager().registerEvents(JoinListener(), this)
            LogService.logColored("Registered Events", ConsoleColors.GREEN)

            Metrics(this, 24460)
            LogService.logColored("Initialized metrics", ConsoleColors.GREEN)

            UpdateHelper.startUpdateCheck()
            LogService.logColored("Started update check", ConsoleColors.GREEN)
            LogService.logColored("                    ", ConsoleColors.GREEN)
        } catch (e: Exception) {
            e.printStackTrace()
            Bukkit.getPluginManager().disablePlugin(this)
        }

    }

    override fun onDisable() {}
}
