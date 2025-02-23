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
import org.bukkit.inventory.InventoryHolder

class InteractListener : Listener {
    @EventHandler
    private fun onInteract(event: PlayerInteractEvent) {
        if (event.isCancelled) return
        if (event.action != Action.LEFT_CLICK_BLOCK) return

        val player = event.player.takeIf { it.hasPermission("ptd.events.interact") } ?: return
        val clickedBlock = event.clickedBlock ?: return
        val item = player.itemInHand.takeIf { it.type != Material.AIR } ?: return
        val arena = BedwarsAPI.getGameAPI().getArenaByPlayer(player) ?: return

        if (clickedBlock.type != Material.ENDER_CHEST && clickedBlock.state !is InventoryHolder ||
            !arena.isInside(clickedBlock.location) ||
            arena.spectators.contains(player)) return

        if (ConfigService.BLACKLISTED_ITEMS.contains(item.type.name)) {
            MessageHelper.sendMessage(player, MessageService.ITEM_BLACKLISTED)
            return
        }
        val blockInventory = InventoryHelper.getInventory(player, clickedBlock) ?: return
        val totalTransfer = if (player.isSneaking) {
            InventoryHelper.transferAllItems(blockInventory, player.inventory, item.type)
        } else {
            InventoryHelper.transferItems(blockInventory, item.type, item.amount).also {
                if (it == item.amount) player.setItemInHand(null)
                else item.amount = (item.amount - it).coerceAtLeast(0)
            }
        }
        if (totalTransfer == 0) return

        MessageHelper.sendMessage(
            player,
            MessageService.TRANSFER_SUCCESS
                .replace("%amount", "$totalTransfer")
                .replace("%item", MessageHelper.formatString(item.type.name))
                .replace("%container", MessageHelper.formatString(clickedBlock.type.name)),
        )
    }
}
