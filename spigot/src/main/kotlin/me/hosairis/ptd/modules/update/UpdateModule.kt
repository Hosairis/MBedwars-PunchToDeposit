package me.hosairis.ptd.modules.update

import me.hosairis.ptd.PunchToDeposit
import me.hosairis.ptd.modules.update.listeners.JoinListener
import me.hosairis.ptd.modules.update.tasks.UpdateCheck
import me.hosairis.ptd.services.ConfigService
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask

class UpdateModule {
    companion object {
        @Volatile
        var isOutDated: Boolean = false
        private var task: BukkitTask? = null

        fun register() {
            if (!ConfigService.UPDATE_CHECK_ENABLED) return
            Bukkit.getPluginManager().registerEvents(JoinListener(), PunchToDeposit.getInst())

            task?.cancel()
            task = UpdateCheck.checkForUpdates()
        }

        fun unregister() {
            task?.cancel()
            task = null
        }
    }
}