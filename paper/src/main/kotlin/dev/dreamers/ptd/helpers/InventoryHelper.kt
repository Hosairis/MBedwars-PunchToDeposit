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

            for ((index, item) in to.contents.withIndex()) {
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

        fun transferAllItems(to: Inventory, from: Inventory, material: Material): Int {
            var totalTransfer = 0

            from.contents.forEachIndexed { index, slot ->
                if (slot?.type == material) {
                    val transferred = InventoryHelper.transferItems(to, material, slot.amount)
                    totalTransfer += transferred

                    if (transferred == slot.amount) {
                        from.setItem(index, null)
                    } else {
                        slot.amount -= transferred
                    }

                    if (transferred > 0) return@forEachIndexed
                }
            }

            return totalTransfer
        }

        fun getInventory(player: Player, clickedBlock: Block): Inventory? {
            val blockType = clickedBlock.type
            val blockState = clickedBlock.state
            val arena = BedwarsAPI.getGameAPI().getArenaByPlayer(player) ?: return null

            return when (blockType) {
                Material.ENDER_CHEST -> arena.getPlayerPrivateInventory(player)
                ConfigService.TEAMCHEST_BLOCK ->
                    if (ConfigService.TEAMCHEST_ENABLED) {
                        arena.getTeamPrivateInventory(arena.getPlayerTeam(player))
                    } else {
                        if (blockState is InventoryHolder) {
                            blockState.inventory
                        } else {
                            null
                        }
                    }

                else -> null
            }
        }
    }
}
