package dev.dreamers.ptd.modules.holo

import de.marcely.bedwars.api.BedwarsAPI
import dev.dreamers.ptd.PunchToDeposit
import dev.dreamers.ptd.modules.holo.listeners.ArenaSaveBlocksListener
import dev.dreamers.ptd.modules.holo.listeners.RoundEndListener
import dev.dreamers.ptd.modules.holo.listeners.RoundStartListener
import dev.dreamers.ptd.modules.holo.tasks.SearchChests
import dev.dreamers.ptd.services.ConfigService
import org.bukkit.Bukkit

class HoloModule {
    companion object {
        val holoList = mutableListOf<HoloData>()

        fun register() {
            if (!ConfigService.HOLOGRAMS_ENABLED) return

            Bukkit.getPluginManager().registerEvents(ArenaSaveBlocksListener(), PunchToDeposit.getInst())
            Bukkit.getPluginManager().registerEvents(RoundStartListener(), PunchToDeposit.getInst())
            Bukkit.getPluginManager().registerEvents(RoundEndListener(), PunchToDeposit.getInst())

            BedwarsAPI.onReady {
                SearchChests.searchChestLocations()
            }
        }
    }
}