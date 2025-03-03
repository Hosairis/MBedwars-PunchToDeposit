package dev.dreamers.ptd.listeners

import de.marcely.bedwars.api.BedwarsAPI
import de.marcely.bedwars.api.arena.ArenaStatus
import dev.dreamers.ptd.events.PostItemDepositEvent
import dev.dreamers.ptd.events.PreItemDepositEvent

import dev.dreamers.ptd.helpers.InventoryHelper
import dev.dreamers.ptd.helpers.MessageHelper
import dev.dreamers.ptd.services.ConfigService
import dev.dreamers.ptd.services.MessageService
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.InventoryHolder

class InteractListener : Listener {
    @EventHandler
    private fun onInteract(event: PlayerInteractEvent) {
        if (event.isCancelled || event.action != Action.LEFT_CLICK_BLOCK) return

        val player = event.player
            .takeIf { it.hasPermission("ptd.events.interact") } ?: return
        val arena = BedwarsAPI.getGameAPI().getArenaByPlayer(player)
            ?.takeIf { it.status == ArenaStatus.RUNNING && player !in it.spectators } ?: return
        val clickedBlock = event.clickedBlock
            ?.takeIf { (it.type == Material.ENDER_CHEST || it.state is InventoryHolder) && arena.isInside(it.location) } ?: return
        val item = player.itemInHand
            .takeIf { it.type != Material.AIR && it.type.name !in ConfigService.BLACKLISTED_ITEMS }
            ?: run {
                if (player.itemInHand.type.name in ConfigService.BLACKLISTED_ITEMS) { MessageHelper.sendMessage(player, MessageService.ITEM_BLACKLISTED) }
                return
            }
        val blockInventory = InventoryHelper.getInventory(player, clickedBlock) ?: return
        val blockInvCapacity = InventoryHelper.getAvailableSpace(blockInventory, item.type)
            .takeIf { it > 0 } ?: return
        val amountToTransfer =
            minOf(
                if (player.isSneaking)
                    InventoryHelper.getTotalItemAmount(player.inventory, item.type)
                else item.amount,
                blockInvCapacity
            )
        PreItemDepositEvent(player, item, amountToTransfer).apply {
            Bukkit.getPluginManager().callEvent(this)
            if (isCancelled) return
        }

        if (player.isSneaking) {
            InventoryHelper.transferAllItems(blockInventory, player.inventory, item.type)
        } else {
            val transferred = InventoryHelper.transferItems(blockInventory, item.type, item.amount)
            player.setItemInHand(if (transferred == item.amount) null else item.apply { amount -= transferred })
        }
        MessageHelper.sendMessage(
            player,
            MessageService.TRANSFER_SUCCESS
                .replace("%amount", "$amountToTransfer")
                .replace("%item", MessageHelper.formatString(item.type.name))
                .replace("%container", MessageHelper.formatString(clickedBlock.type.name)),
        )
        PostItemDepositEvent(player, item, amountToTransfer).apply {
            Bukkit.getPluginManager().callEvent(this)
        }
    }
}
