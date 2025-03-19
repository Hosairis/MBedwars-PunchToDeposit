package dev.dreamers.ptd.modules.deposit.listeners

import dev.dreamers.ptd.modules.deposit.DepositModule
import dev.dreamers.ptd.services.LogService
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class QuitListener: Listener {
    @EventHandler
    private fun onQuit(event: PlayerQuitEvent) {
        DepositModule.doubleClickPlayers[event.player]?.cancel()
        DepositModule.doubleClickPlayers.remove(event.player)
        LogService.debug("DC | ${event.player.name} Removed: Quit")
    }
}