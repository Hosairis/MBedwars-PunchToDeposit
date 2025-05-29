package me.hosairis.ptd.modules.holo

import de.marcely.bedwars.api.arena.Arena
import de.marcely.bedwars.api.arena.Team
import de.marcely.bedwars.api.event.player.PlayerOpenArenaChestEvent.ChestType
import de.marcely.bedwars.api.world.hologram.skin.HolographicHologramSkin
import org.bukkit.entity.Player

data class HoloData(
    val arena: Arena,
    val team: Team,
    val privateHolo: HolographicHologramSkin?, // Nullable
    val teamHolo: HolographicHologramSkin?,    // Nullable
    var privateHoloPlayers: MutableSet<Player> = mutableSetOf(),
    var teamHoloPlayers: MutableSet<Player> = mutableSetOf()
) {
    fun removePlayer(player: Player, chestType: ChestType? = null) {
        if (chestType == null) {
            removePlayerFromPrivateHolo(player)
            removePlayerFromTeamHolo(player)
        } else {
            if (chestType == ChestType.PRIVATE) {
                removePlayerFromPrivateHolo(player)
            } else {
                removePlayerFromTeamHolo(player)
            }
        }
    }

    fun remove() {
        privateHolo?.hologram?.remove()
        teamHolo?.hologram?.remove()
    }

    private fun removePlayerFromPrivateHolo(player: Player) {
        privateHoloPlayers.remove(player)
        if (privateHoloPlayers.isEmpty()) privateHolo?.hologram?.remove()
    }

    private fun removePlayerFromTeamHolo(player: Player) {
        teamHoloPlayers.remove(player)
        if (teamHoloPlayers.isEmpty()) teamHolo?.hologram?.remove()
    }

    fun setHoloPredicate() {
        privateHolo?.hologram?.setPerPlayerVisibility { p ->
            privateHoloPlayers.contains(p)
        }

        teamHolo?.hologram?.setPerPlayerVisibility { p ->
            teamHoloPlayers.contains(p)
        }
    }
}

data class ChestData(var privateChest: Boolean = false, var teamChest: Boolean = false)
data class TeamData(
    var x: Int,
    var y: Int,
    var z: Int,
    val minX: Int,
    val minY: Int,
    val minZ: Int,
    val maxX: Int,
    val maxY: Int,
    val maxZ: Int,
    val arena: Arena,
    val team: Team,
    var chestStatus: ChestData = ChestData()
) {
    fun increment(): Boolean {
        if (++x > maxX) {
            x = minX
            if (++y > maxY) {
                y = minY
                if (++z > maxZ) {
                    return false // Done with this region
                }
            }
        }
        return true // Not done yet
    }
}