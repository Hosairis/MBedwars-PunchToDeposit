package dev.dreamers.ptd.modules.deposit

import dev.dreamers.ptd.PunchToDeposit
import dev.dreamers.ptd.modules.deposit.listeners.InteractListener
import dev.dreamers.ptd.modules.deposit.listeners.QuitListener
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask

class DepositModule {
    companion object {
        val doubleClickPlayers: MutableMap<Player, BukkitTask> = mutableMapOf()

        fun register() {
            Bukkit.getPluginManager().registerEvents(InteractListener(), PunchToDeposit.getInst())
            Bukkit.getPluginManager().registerEvents(QuitListener(), PunchToDeposit.getInst())
        }
    }
}