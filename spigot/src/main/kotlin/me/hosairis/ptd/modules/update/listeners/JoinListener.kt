package me.hosairis.ptd.modules.update.listeners

import me.hosairis.ptd.helpers.MessageHelper
import me.hosairis.ptd.modules.update.UpdateModule
import me.hosairis.ptd.services.MessageService
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class JoinListener: Listener {
    @EventHandler
    private fun onJoin(event: PlayerJoinEvent) {
        if (!event.player.hasPermission("ptd.events.notifyupdate")) return
        if (UpdateModule.isOutDated) MessageHelper.sendMessage(event.player, MessageService.UPDATE_FOUND)
    }
}