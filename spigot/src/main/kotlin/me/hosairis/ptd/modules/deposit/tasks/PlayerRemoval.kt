package me.hosairis.ptd.modules.deposit.tasks

import me.hosairis.ptd.MBPunchToDeposit
import me.hosairis.ptd.modules.deposit.DepositModule
import me.hosairis.ptd.services.ConfigService
import me.hosairis.ptd.services.LogService
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
            }.runTaskLater(MBPunchToDeposit.getInst(), ConfigService.DOUBLE_CLICK_TIME)
        }
    }
}