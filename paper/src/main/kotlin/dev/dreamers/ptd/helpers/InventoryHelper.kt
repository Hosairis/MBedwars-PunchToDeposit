package dev.dreamers.ptd.helpers

import de.marcely.bedwars.api.BedwarsAPI
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.Chest
import org.bukkit.block.Container
import org.bukkit.block.EnderChest
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class InventoryHelper {
    companion object {
        fun transferItems(to: Inventory, material: Material, amount: Int): Int {
            if (amount <= 0) return 0

            val maxStackSize = material.maxStackSize
            var remaining = amount
            var transferred = 0

            for ((index, item) in to.storageContents.withIndex()) {
                if (remaining == 0) break

                when {
                    item?.type == material && item.amount < maxStackSize -> {
                        val toAdd = minOf(maxStackSize - item.amount, remaining)
                        item.amount += toAdd
                        remaining -= toAdd
                        transferred += toAdd
                    }

                    item == null -> {
                        val toAdd = minOf(maxStackSize, remaining)
                        to.setItem(index, ItemStack(material, toAdd))
                        remaining -= toAdd
                        transferred += toAdd
                    }
                }
            }

            return transferred
        }


        fun getInventory(player: Player, clickedBlock: Block): Inventory? {
            val blockState = clickedBlock.state

            if (!HookHelper.usesMBedwars) {
                return when (blockState) {
                    is EnderChest -> player.enderChest
                    is Container -> blockState.inventory
                    else -> null
                }
            }

            val arena =
                BedwarsAPI.getGameAPI().getArenaByPlayer(player)
                    ?: return when (blockState) {
                        is EnderChest -> player.enderChest
                        is Container -> blockState.inventory
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
