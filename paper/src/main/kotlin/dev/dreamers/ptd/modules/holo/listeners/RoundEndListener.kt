package dev.dreamers.ptd.modules.holo.listeners

import de.marcely.bedwars.api.event.arena.RoundEndEvent
import dev.dreamers.ptd.modules.holo.HoloModule
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class RoundEndListener: Listener {
    @EventHandler
    private fun onRoundEnd(event: RoundEndEvent) {
        HoloModule.holoList.removeIf { it.arena == event.arena }
    }
}