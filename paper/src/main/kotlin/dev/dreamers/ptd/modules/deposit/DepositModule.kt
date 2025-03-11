package dev.dreamers.ptd.modules.deposit

import dev.dreamers.ptd.PunchToDeposit
import dev.dreamers.ptd.modules.deposit.listeners.InteractListener
import org.bukkit.Bukkit

class DepositModule {
    companion object {
        fun register() {
            Bukkit.getPluginManager().registerEvents(InteractListener(), PunchToDeposit.getInst())
        }
    }
}