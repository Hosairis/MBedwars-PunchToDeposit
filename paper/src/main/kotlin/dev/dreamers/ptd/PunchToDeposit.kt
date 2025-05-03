package dev.dreamers.ptd

import dev.dreamers.ptd.commands.PTDCommand
import dev.dreamers.ptd.commands.PTDCompletions
import dev.dreamers.ptd.helpers.MessageHelper
import dev.dreamers.ptd.services.ConfigService
import dev.dreamers.ptd.services.LogService
import dev.dreamers.ptd.services.MessageService
import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class PunchToDeposit : JavaPlugin() {

    companion object {
        private lateinit var plugin: PunchToDeposit
        private lateinit var addon: PunchToDepositAddon
        private lateinit var metrics: Metrics

        const val PLUGIN_NAME = "PunchToDeposit"
        const val PLUGIN_VERSION = "1.3.1"

        const val MIN_BW_API_VER = 200
        const val MIN_BW_VER = "5.5.1"

        fun getInst(): PunchToDeposit {
            return plugin
        }

        fun getAddon(): PunchToDepositAddon {
            return addon
        }
    }

    override fun onEnable() {
        try {
            plugin = this

            if (!checkMBedwars()) return
            if (!registerAddon()) return

            ConfigService.init()
            MessageService.init()

            addon.registerModules()

            val command = getCommand("ptd")
            command?.setExecutor(PTDCommand())
            command?.setTabCompleter(PTDCompletions())

            metrics = Metrics(plugin, 25165)

            MessageHelper.printSplashScreen()
        } catch (e: Exception) {
            e.printStackTrace()
            Bukkit.getPluginManager().disablePlugin(this)
        }
    }

    override fun onDisable() {
        metrics.shutdown()
        addon.unregisterModules()
    }

    private fun checkMBedwars(): Boolean {
        return try {
            val apiClass = Class.forName("de.marcely.bedwars.api.BedwarsAPI")
            val apiVersion = apiClass.getMethod("getAPIVersion").invoke(null) as Int

            if (apiVersion < MIN_BW_API_VER) {
                throw IllegalStateException()
            }
            true
        } catch (e: Exception) {
            LogService.severe("Unsupported MBedwars version detected, Please update to v$MIN_BW_VER")
            Bukkit.getPluginManager().disablePlugin(this)
            false
        }
    }

    private fun registerAddon(): Boolean {
        addon = PunchToDepositAddon(this)

        return if (!addon.register()) {
            LogService.severe("An error occurred, Please check for duplicate addons and remove them")
            Bukkit.getPluginManager().disablePlugin(this)
            false
        } else {
            true
        }
    }
}
