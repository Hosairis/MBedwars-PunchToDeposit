package me.hosairis.ptd.helpers

import de.marcely.bedwars.api.arena.Arena
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import kotlin.math.min

class InventoryHelper {
    companion object {
        fun getAvailableSpace(inventory: Inventory, material: Material): Int {
            val maxStackSize = material.maxStackSize
            var availableSpace = 0

            for (item in inventory.contents) {
                when {
                    item == null || item.type == Material.AIR -> {
                        // Empty slot can hold a full stack
                        availableSpace += maxStackSize
                    }

                    item.type == material && item.amount < maxStackSize -> {
                        // Partial stack can hold more items
                        availableSpace += maxStackSize - item.amount
                    }
                }
            }
            return availableSpace
        }

        fun getTotalItemAmount(inventory: Inventory, material: Material): Int {
            return inventory.contents.filter { it?.type == material }.sumOf { it?.amount ?: 0 }
        }

        /**
         * Adds items to an inventory, filling partial stacks first, with optimized performance
         * @param inventory Target inventory
         * @param itemStack Item type to add
         * @param amount Number of items to attempt adding
         * @return Number of items successfully added
         */
        fun addItemsToInventory(inventory: Inventory, itemStack: ItemStack, amount: Int): Int {
            if (amount <= 0 || itemStack.maxStackSize <= 0) return 0

            val maxStackSize = itemStack.maxStackSize
            var remaining = amount
            var added = 0

            val contents = inventory.contents ?: return 0

            var i = 0
            while (i < contents.size && remaining > 0) {
                val slot = contents[i]
                if (slot != null && slot.isSimilar(itemStack) && slot.amount < maxStackSize) {
                    val space = maxStackSize - slot.amount
                    val toAdd = min(space, remaining)
                    slot.amount += toAdd
                    added += toAdd
                    remaining -= toAdd
                }
                i++
            }

            if (remaining > 0) {
                val fullStacks = remaining / maxStackSize
                val leftover = remaining % maxStackSize

                val toAdd = itemStack.clone()

                if (fullStacks > 0) {
                    toAdd.amount = maxStackSize
                    val stacksToAdd = Array(fullStacks) { toAdd.clone() }
                    val leftoverMap = inventory.addItem(*stacksToAdd)

                    if (leftoverMap.isEmpty()) {
                        added += fullStacks * maxStackSize
                    } else {
                        val addedStacks = fullStacks - leftoverMap.size
                        added += addedStacks * maxStackSize
                        leftoverMap.values.firstOrNull()?.let { remainingStack ->
                            added += maxStackSize - remainingStack.amount
                        }
                        return added
                    }
                }

                if (leftover > 0) {
                    toAdd.amount = leftover
                    val leftoverMap = inventory.addItem(toAdd)
                    if (leftoverMap.isEmpty()) {
                        added += leftover
                    } else {
                        leftoverMap.values.firstOrNull()?.let { remainingStack ->
                            added += leftover - remainingStack.amount
                        }
                    }
                }
            }

            return added
        }

        /**
         * Transfers all items matching the given ItemStack type from one inventory to another
         * @param toInventory Destination inventory
         * @param fromInventory Source inventory
         * @param itemTemplate Template ItemStack defining the item type to transfer
         * @return Total number of items transferred
         */
        fun addAllItemsToInventory(toInventory: Inventory, fromInventory: Inventory, itemTemplate: ItemStack): Int {
            if (itemTemplate.amount <= 0 || itemTemplate.maxStackSize <= 0 || !fromInventory.isOccupied()) return 0

            val fromContents = fromInventory.contents ?: return 0

            var totalAvailable = 0
            for (slot in fromContents) {
                if (slot != null && slot.isSimilar(itemTemplate)) {
                    totalAvailable += slot.amount
                }
            }

            if (totalAvailable == 0) return 0

            return addItemsToInventory(toInventory, itemTemplate, totalAvailable)
        }

        fun getInventory(arena: Arena, player: Player, block: Block): Inventory? {
            arena.getChestType(block) ?: return null
            return arena.getChestInventory(block, player)
        }

        private fun Inventory.isOccupied(): Boolean {
            val contents = this.contents
            for (i in contents.indices) {
                val item = contents[i]
                if (item != null && item.type != Material.AIR) return true
            }
            return false
        }
    }
}
