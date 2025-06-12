package me.hosairis.ptd.modules.holo.listeners

import de.marcely.bedwars.api.arena.ArenaStatus
import de.marcely.bedwars.api.event.arena.ArenaStatusChangeEvent
import me.hosairis.ptd.modules.holo.HoloModule
import me.hosairis.ptd.services.LogService
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ArenaStatusChangeListener: Listener {
    @EventHandler
    private fun onRoundEnd(event: ArenaStatusChangeEvent) {
        if (event.oldStatus == ArenaStatus.RUNNING) {
            LogService.debug("Rem holograms for ${event.arena.name}: ${event.oldStatus}")
            LogService.debug("B Rem: ${HoloModule.holoList.size}")
            val iterator = HoloModule.holoList.iterator()
            while (iterator.hasNext()) {
                val holo = iterator.next()
                if (holo.arena == event.arena) {
                    holo.remove()
                    iterator.remove()
                }
            }
        }
        LogService.debug("A Rem: ${HoloModule.holoList.size}")
    }
}