package dev.dreamers.ptd.modules.holo.tasks

import de.marcely.bedwars.api.BedwarsAPI
import de.marcely.bedwars.api.arena.Arena
import de.marcely.bedwars.api.event.player.PlayerOpenArenaChestEvent.ChestType
import dev.dreamers.ptd.PunchToDeposit
import dev.dreamers.ptd.modules.holo.TeamData
import dev.dreamers.ptd.services.ConfigService
import dev.dreamers.ptd.services.LogService
import org.bukkit.scheduler.BukkitRunnable

class SearchChests {
    companion object {
        fun searchChestLocations(vararg arenas: Arena?) {
            val teamTracks = mutableListOf<TeamData>(). apply { clear() }

            val arenaList = if (arenas.isEmpty()) {
                BedwarsAPI.getGameAPI().arenas.takeIf { it.isNotEmpty() } ?: run {
                    LogService.debug("No arenas")
                    return
                }
            } else {
                arenas.filterNotNull().toList()
            }

            for (arena in arenaList) {
                val world = arena.gameWorld ?: continue
                for (team in arena.enabledTeams) {
                    val spawnLocation = arena.getTeamSpawn(team) ?: continue
                    val minX = spawnLocation.blockX - ConfigService.HOLOGRAMS_X_SEARCH
                    val minY = (spawnLocation.blockY - ConfigService.HOLOGRAMS_Y_SEARCH).coerceAtLeast(-64)
                    val minZ = spawnLocation.blockZ - ConfigService.HOLOGRAMS_Z_SEARCH
                    val maxX = spawnLocation.blockX + ConfigService.HOLOGRAMS_X_SEARCH
                    val maxY = (spawnLocation.blockY + ConfigService.HOLOGRAMS_Y_SEARCH).coerceAtMost(world.maxHeight)
                    val maxZ = spawnLocation.blockZ + ConfigService.HOLOGRAMS_Z_SEARCH

                    teamTracks.add(
                        TeamData(
                            x = minX, y = minY, z = minZ,
                            minX = minX, minY = minY, minZ = minZ,
                            maxX = maxX, maxY = maxY, maxZ = maxZ,
                            arena = arena,
                            team = team
                        )
                    )
                }
            }
            LogService.debug("Init: ${teamTracks.size}")

            object : BukkitRunnable() {
                override fun run() {
                    val startTime = System.currentTimeMillis()
                    if (teamTracks.isEmpty()) {
                        LogService.debug("Done")
                        cancel()
                        return
                    }
                    val trackedTeam = teamTracks.first()
                    val arena = trackedTeam.arena
                    val world = arena.gameWorld ?: run {
                        LogService.debug("Skip: ${arena.name}")
                        teamTracks.removeFirst()
                        return
                    }
                    val team = trackedTeam.team
                    val chestStatus = trackedTeam.chestStatus

                    var coordinatesProcessed = 0

                    while (System.currentTimeMillis() - startTime < 5) {
                        val x = trackedTeam.x
                        val y = trackedTeam.y
                        val z = trackedTeam.z
                        val block = arena.getChestType(world.getBlockAt(x, y, z))

                        when(block) {
                            null -> {}
                            ChestType.PRIVATE -> {
                                if (!chestStatus.privateChest) {
                                    chestStatus.privateChest = true
                                    arena.persistentStorage.set("${team.name}_privateChestLoc", "$x,$y,$z")
                                    LogService.debug("P: $x,$y,$z")
                                }
                            }
                            ChestType.TEAM, ChestType.VANILLA -> {
                                if (!chestStatus.teamChest) {
                                    chestStatus.teamChest = true
                                    arena.persistentStorage.set("${team.name}_teamChestLoc", "$x,$y,$z")
                                    LogService.debug("T: $x,$y,$z")
                                }
                            }
                        }
                        coordinatesProcessed++

                        if (chestStatus.privateChest && chestStatus.teamChest) {
                            LogService.debug("Done: Both")
                            teamTracks.removeFirst()
                            break
                        }
                        if (!trackedTeam.increment()) {
                            LogService.debug("Done: Scanned")
                            teamTracks.removeFirst()
                            break
                        }
                    }
                    LogService.debug("Proc: $coordinatesProcessed, ${System.currentTimeMillis() - startTime}ms")
                    LogService.debug("Rem: ${teamTracks.size}")
                }
            }.runTaskTimer(PunchToDeposit.getInst(), 0, 1)
        }
    }
}