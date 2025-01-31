package dev.dreamers.ptd.helpers

import de.marcely.bedwars.api.BedwarsAPI
import org.bukkit.block.Block
import org.bukkit.block.Chest
import org.bukkit.block.EnderChest
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

class InventoryHelper {
    companion object {
        fun getInventory(player: Player, clickedBlock: Block): Inventory? {
            val blockState = clickedBlock.state

            if (!HookHelper.usesMBedwars) {
                return when (blockState) {
                    is Chest -> blockState.inventory
                    is EnderChest -> player.enderChest
                    else -> null
                }
            }

            val arena =
                BedwarsAPI.getGameAPI().getArenaByPlayer(player)
                    ?: return when (blockState) {
                        is Chest -> blockState.inventory
                        is EnderChest -> player.enderChest
                        else -> null
                    }

            return when (blockState) {
                is Chest -> arena.getTeamPrivateInventory(arena.getPlayerTeam(player))
                is EnderChest -> arena.getPlayerPrivateInventory(player)
                else -> null
            }
        }
    }
}
