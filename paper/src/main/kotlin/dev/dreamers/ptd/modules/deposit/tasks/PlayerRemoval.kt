package dev.dreamers.ptd.modules.deposit.tasks

import dev.dreamers.ptd.PunchToDeposit
import dev.dreamers.ptd.modules.deposit.DepositModule
import dev.dreamers.ptd.services.ConfigService
import dev.dreamers.ptd.services.LogService
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class PlayerRemoval {
    companion object {
        fun schedulePlayerRemoval(player: Player): BukkitTask {
            return object : BukkitRunnable() {
                override fun run() {
                    DepositModule.doubleClickPlayers.remove(player)
                    LogService.debug("DC | ${player.name} Removed: Expired")
                }
            }.runTaskLater(PunchToDeposit.getInst(), ConfigService.DOUBLE_CLICK_TIME)
        }
    }
}