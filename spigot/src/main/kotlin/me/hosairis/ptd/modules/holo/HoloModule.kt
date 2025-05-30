package me.hosairis.ptd.modules.holo

import de.marcely.bedwars.api.BedwarsAPI
import me.hosairis.ptd.MBPunchToDeposit
import me.hosairis.ptd.modules.holo.listeners.ArenaSaveBlocksListener
import me.hosairis.ptd.modules.holo.listeners.RoundEndListener
import me.hosairis.ptd.modules.holo.listeners.RoundStartListener
import me.hosairis.ptd.modules.holo.tasks.SearchChests
import me.hosairis.ptd.services.ConfigService
import org.bukkit.Bukkit

class HoloModule {
    companion object {
        val holoList = mutableListOf<HoloData>()

        fun register() {
            if (!ConfigService.HOLOGRAMS_ENABLED) return

            Bukkit.getPluginManager().registerEvents(ArenaSaveBlocksListener(), MBPunchToDeposit.getInst())
            Bukkit.getPluginManager().registerEvents(RoundStartListener(), MBPunchToDeposit.getInst())
            Bukkit.getPluginManager().registerEvents(RoundEndListener(), MBPunchToDeposit.getInst())

            BedwarsAPI.onReady {
                SearchChests.searchChestLocations()
            }
        }
    }
}