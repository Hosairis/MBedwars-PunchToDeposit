package dev.dreamers.ptd.listeners

import dev.dreamers.ptd.helpers.InventoryHelper
import dev.dreamers.ptd.helpers.MessageHelper
import dev.dreamers.ptd.services.ConfigService
import dev.dreamers.ptd.services.MessageService
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class InteractListener : Listener {
    @EventHandler
    private fun onInteract(event: PlayerInteractEvent) {
        if (event.isCancelled) return
        val player = event.player
        val block = event.clickedBlock ?: return

        if (event.action != Action.LEFT_CLICK_BLOCK ||
            !player.hasPermission("ptd.events.interact") ||
            player.inventory.itemInHand.type == Material.AIR) return

        val item = player.inventory.itemInHand
        val itemType = item.type
        if (ConfigService.BLACKLISTED_ITEMS.contains(itemType.name)) {
            MessageHelper.sendMessage(player, MessageService.ITEM_BLACKLISTED)
            return
        }
        if (ConfigService.BLACKLISTED_CONTAINERS.contains(block.type.name)) {
            MessageHelper.sendMessage(player, MessageService.CONTAINER_BLACKLISTED)
            return
        }

        val blockInventory = InventoryHelper.getInventory(player, block) ?: return
        var totalTransfer = 0

        if (player.isSneaking) {
            player.inventory.contents.forEachIndexed { index, slot ->
                if (slot?.type == itemType) {
                    val transferred =
                        InventoryHelper.transferItems(blockInventory, itemType, slot.amount)
                    totalTransfer += transferred

                    if (transferred == slot.amount) {
                        player.inventory.setItem(index, null)
                    } else {
                        slot.amount -= transferred
                    }

                    if (transferred > 0) return@forEachIndexed
                }
            }
        } else {
            totalTransfer = InventoryHelper.transferItems(blockInventory, itemType, item.amount)
            if (totalTransfer == item.amount) {
                player.setItemInHand(null)
            } else {
                item.amount = (item.amount - totalTransfer).coerceAtLeast(0)
            }
        }

        if (totalTransfer == 0) return

        MessageHelper.sendMessage(
            player,
            MessageService.TRANSFER_SUCCESS.replace("%amount", "$totalTransfer")
                .replace("%item", MessageHelper.formatString(itemType.name))
                .replace("%container", MessageHelper.formatString(block.type.name)))
    }
}
