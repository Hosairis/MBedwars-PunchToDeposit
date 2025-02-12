package dev.dreamers.ptd.helpers

import de.marcely.bedwars.api.BedwarsAPI
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.Chest
import org.bukkit.block.Container
import org.bukkit.block.Dispenser
import org.bukkit.block.Dropper
import org.bukkit.block.EnderChest
import org.bukkit.block.Furnace
import org.bukkit.block.Hopper
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


        fun getInventory(player: Player, clickedBlock: Block): Inventory? {
            val blockType = clickedBlock.type
            val blockState = clickedBlock.state

            val isLegacy = VersionHelper.isOlderThan(VersionHelper.Version.V1_13)

            // Common inventory retrieval logic for non-Bedwars cases
            fun getDefaultInventory(): Inventory? = when {
                blockType == Material.ENDER_CHEST -> player.enderChest
                isLegacy -> when (blockType) {
                    Material.CHEST, Material.TRAPPED_CHEST -> (blockState as? Chest)?.blockInventory
                    Material.FURNACE -> (blockState as? Furnace)?.inventory
                    Material.DISPENSER -> (blockState as? Dispenser)?.inventory
                    Material.HOPPER -> (blockState as? Hopper)?.inventory
                    Material.DROPPER -> (blockState as? Dropper)?.inventory
                    else -> null
                }
                else -> when (blockState) {
                    is EnderChest -> player.enderChest
                    is Container -> blockState.inventory
                    else -> null
                }
            }

            if (!HookHelper.usesMBedwars) return getDefaultInventory()

            // Fetch arena only once
            val arena = BedwarsAPI.getGameAPI().getArenaByPlayer(player) ?: return getDefaultInventory()

            return when {
                blockType == Material.ENDER_CHEST -> arena.getPlayerPrivateInventory(player)
                isLegacy -> when (blockType) {
                    Material.CHEST, Material.TRAPPED_CHEST -> arena.getTeamPrivateInventory(arena.getPlayerTeam(player))
                    else -> null
                }
                blockState is Chest -> arena.getTeamPrivateInventory(arena.getPlayerTeam(player))
                else -> null
            }
        }

    }
}
