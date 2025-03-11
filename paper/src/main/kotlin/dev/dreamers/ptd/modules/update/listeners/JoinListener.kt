package dev.dreamers.ptd.modules.update.listeners

import dev.dreamers.ptd.helpers.MessageHelper
import dev.dreamers.ptd.modules.update.UpdateModule
import dev.dreamers.ptd.services.MessageService
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