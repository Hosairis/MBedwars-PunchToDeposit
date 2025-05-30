package me.hosairis.ptd.modules.deposit

import me.hosairis.ptd.MBPunchToDeposit
import me.hosairis.ptd.modules.deposit.listeners.InteractListener
import me.hosairis.ptd.modules.deposit.listeners.QuitListener
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask

class DepositModule {
    companion object {
        val doubleClickPlayers: MutableMap<Player, BukkitTask> = mutableMapOf()

        fun register() {
            Bukkit.getPluginManager().registerEvents(InteractListener(), MBPunchToDeposit.getInst())
            Bukkit.getPluginManager().registerEvents(QuitListener(), MBPunchToDeposit.getInst())
        }
    }
}