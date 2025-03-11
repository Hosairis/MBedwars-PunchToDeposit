package dev.dreamers.ptd.modules.update

import dev.dreamers.ptd.PunchToDeposit
import dev.dreamers.ptd.modules.update.listeners.JoinListener
import dev.dreamers.ptd.modules.update.tasks.UpdateCheck
import dev.dreamers.ptd.services.ConfigService
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