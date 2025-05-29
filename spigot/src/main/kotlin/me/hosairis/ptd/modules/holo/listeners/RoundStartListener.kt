package me.hosairis.ptd.modules.holo.listeners

import de.marcely.bedwars.api.BedwarsAPI
import de.marcely.bedwars.api.event.arena.RoundStartEvent
import de.marcely.bedwars.api.world.hologram.HologramSkinType
import de.marcely.bedwars.api.world.hologram.skin.HolographicHologramSkin
import me.hosairis.ptd.modules.holo.HoloData
import me.hosairis.ptd.modules.holo.HoloModule
import me.hosairis.ptd.services.ConfigService
import me.hosairis.ptd.services.LogService
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class RoundStartListener: Listener {
    @EventHandler
    private fun onRoundStart(event: RoundStartEvent) {
        LogService.debug("Round start")
        val arena = event.arena
        val worldStorage = BedwarsAPI.getWorldStorage(arena.gameWorld) ?: return

        for (team in arena.aliveTeams) {
            LogService.debug("Team: ${team.name}")
            val teamChestLocOpt = arena.persistentStorage.get("${team.name}_teamChestLoc")
            val teamHolo = teamChestLocOpt.map { locStr ->
                val locArgs = locStr.split(",")
                val x = locArgs[0].toDouble() + ConfigService.HOLOGRAMS_OFFSET_X
                val y = locArgs[1].toDouble() + ConfigService.HOLOGRAMS_OFFSET_Y
                val z = locArgs[2].toDouble() + ConfigService.HOLOGRAMS_OFFSET_Z
                LogService.debug("Team chest loc: $x, $y, $z")

                val holoEntity = worldStorage.spawnHologram(
                    HologramSkinType.HOLOGRAM,
                    Location(arena.gameWorld, x, y, z)
                )
                val hologram = holoEntity.skin as HolographicHologramSkin
                hologram.lines = ConfigService.HOLOGRAMS_TEXT

                hologram
            }.orElse(null)

            val privateChestLocOpt = arena.persistentStorage.get("${team.name}_privateChestLoc")
            val privateHolo = privateChestLocOpt.map { locStr ->
                val locArgs = locStr.split(",")
                val x = locArgs[0].toDouble() + ConfigService.HOLOGRAMS_OFFSET_X
                val y = locArgs[1].toDouble() + ConfigService.HOLOGRAMS_OFFSET_Y
                val z = locArgs[2].toDouble() + ConfigService.HOLOGRAMS_OFFSET_Z
                LogService.debug("Private chest loc: $x, $y, $z")

                val holoEntity = worldStorage.spawnHologram(
                    HologramSkinType.HOLOGRAM,
                    Location(arena.gameWorld, x, y, z)
                )
                val hologram = holoEntity.skin as HolographicHologramSkin
                hologram.lines = ConfigService.HOLOGRAMS_TEXT

                hologram
            }.orElse(null)

            if (teamHolo != null || privateHolo != null) {
                val players = arena.players.filter { arena.getPlayerTeam(it) == team }.toMutableSet()
                val tracker = HoloData(arena, team, privateHolo, teamHolo, players, players)
                HoloModule.holoList.add(tracker)
                tracker.setHoloPredicate()
            }
        }
    }
}