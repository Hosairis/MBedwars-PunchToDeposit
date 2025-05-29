package me.hosairis.ptd.modules.holo.listeners

import de.marcely.bedwars.api.event.arena.ArenaSaveBlocksStopEvent
import me.hosairis.ptd.modules.holo.tasks.SearchChests
import me.hosairis.ptd.services.LogService
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ArenaSaveBlocksListener: Listener {
    @EventHandler
    private fun onSaveBlock(event: ArenaSaveBlocksStopEvent) {
        if (!event.isSuccess) {
            LogService.debug("Unsuccessful arena update detected, skipping")
            return
        }
        LogService.debug("Detected arena update, fetching new holo locations")
        SearchChests.searchChestLocations(event.arena)
    }
}