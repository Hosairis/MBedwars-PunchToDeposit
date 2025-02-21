package dev.dreamers.ptd.listeners

import de.marcely.bedwars.api.BedwarsAPI
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
        if (
            event.isCancelled ||
            !event.player.hasPermission("ptd.events.interact") ||
            event.action != Action.LEFT_CLICK_BLOCK ||
            event.clickedBlock == null ||
            !(event.clickedBlock!!.type == Material.valueOf(ConfigService.TEAMCHEST_BLOCK) || event.clickedBlock!!.type == Material.ENDER_CHEST) ||
            event.player.itemInHand.type == Material.AIR ||
            BedwarsAPI.getGameAPI().getArenaByPlayer(event.player) == null ||
            BedwarsAPI.getGameAPI().getArenaBySpectator(event.player) != null
        ) return
        val player = event.player
        val block = event.clickedBlock!!
        val item = player.itemInHand

        if (ConfigService.BLACKLISTED_ITEMS.contains(item.type.name)) {
            MessageHelper.sendMessage(player, MessageService.ITEM_BLACKLISTED)
            return
        }
        val blockInventory = InventoryHelper.getInventory(player, block) ?: return
        var totalTransfer = 0

        if (player.isSneaking) {
            totalTransfer = InventoryHelper.transferAllItems(blockInventory, player.inventory, item.type)
        } else {
            totalTransfer = InventoryHelper.transferItems(blockInventory, item.type, item.amount)
            if (totalTransfer == item.amount) {
                player.setItemInHand(null)
            } else {
                item.amount = (item.amount - totalTransfer).coerceAtLeast(0)
            }
        }
        MessageHelper.sendMessage(
            player,
            MessageService.TRANSFER_SUCCESS.replace("%amount", "$totalTransfer")
                .replace("%item", MessageHelper.formatString(item.type.name))
                .replace("%container", MessageHelper.formatString(block.type.name)))
    }
}
