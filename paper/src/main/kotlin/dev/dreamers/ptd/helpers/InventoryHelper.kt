package dev.dreamers.ptd.helpers

import de.marcely.bedwars.api.BedwarsAPI
import dev.dreamers.ptd.services.ConfigService
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

class InventoryHelper {
    companion object {
        fun transferItems(to: Inventory, material: Material, amount: Int): Int {
            if (amount <= 0) return 0

            val maxStackSize = material.maxStackSize
            var remaining = amount
            var transferred = 0

            to.contents.forEachIndexed { index, item ->
                if (remaining == 0) return@forEachIndexed

                val toAdd = when {
                    item?.type == material && item.amount < maxStackSize ->
                        minOf(maxStackSize - item.amount, remaining).also { item.amount += it }

                    item == null ->
                        minOf(maxStackSize, remaining).also { to.setItem(index, ItemStack(material, it)) }

                    else -> 0
                }

                remaining -= toAdd
                transferred += toAdd
            }

            return transferred
        }

        fun transferAllItems(to: Inventory, from: Inventory, material: Material): Int {
            var totalTransfer = 0

            from.contents.forEachIndexed { index, slot ->
                slot?.takeIf { it.type == material }?.let {
                    val transferred = transferItems(to, material, it.amount)
                    totalTransfer += transferred

                    if (transferred == it.amount) from.setItem(index, null)
                    else it.amount -= transferred

                    if (transferred > 0) return@forEachIndexed
                }
            }

            return totalTransfer
        }

        fun getInventory(player: Player, clickedBlock: Block): Inventory? {
            val arena = BedwarsAPI.getGameAPI().getArenaByPlayer(player) ?: return null
            val blockType = clickedBlock.type
            val blockState = clickedBlock.state

            return when {
                blockType == Material.ENDER_CHEST ->
                    arena.getPlayerPrivateInventory(player)

                ConfigService.TEAMCHEST_ENABLED ->
                    if (blockType == ConfigService.TEAMCHEST_BLOCK)
                        arena.getTeamPrivateInventory(arena.getPlayerTeam(player))
                    else null

                else -> (blockState as? InventoryHolder)?.inventory
                    ?.takeIf { ConfigService.INTERACTING || blockType in listOf(Material.CHEST, Material.TRAPPED_CHEST) }
            }
        }
    }
}
