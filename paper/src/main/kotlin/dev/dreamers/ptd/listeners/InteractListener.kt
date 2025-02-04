package dev.dreamers.ptd.listeners

import dev.dreamers.ptd.helpers.InventoryHelper
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class InteractListener : Listener {
    @EventHandler
    private fun onInteract(event: PlayerInteractEvent) {
        if (event.action != Action.LEFT_CLICK_BLOCK) return
        if (!event.player.hasPermission("ptd.events.interact")) return
        if (
            event.clickedBlock == null ||
                (event.clickedBlock?.type != Material.CHEST &&
                    event.clickedBlock?.type != Material.ENDER_CHEST)
        )
            return
        if (event.player.itemInHand.type == Material.AIR) return

        val player = event.player
        val block = event.clickedBlock
        val blockInventory = InventoryHelper.getInventory(player, block!!) ?: return
        val item = player.itemInHand

        if (player.isSneaking) {
            for (slot in 0 until player.inventory.size) {
                val inventoryItem = player.inventory.getItem(slot) ?: continue
                if (inventoryItem.type != item.type) continue

                val remainingItems = blockInventory.addItem(inventoryItem)
                player.inventory.setItem(slot, null)

                if (remainingItems.isNotEmpty()) {
                    player.inventory.setItem(slot, remainingItems.values.first())
                    break
                }
            }
        } else {
            player.setItemInHand(null)
            val remainingItems = blockInventory.addItem(item)

            if (remainingItems.isEmpty()) return
            remainingItems.forEach { player.inventory.addItem(it.value) }
        }
    }
}
