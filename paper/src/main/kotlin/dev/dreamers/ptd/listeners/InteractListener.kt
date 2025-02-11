package dev.dreamers.ptd.listeners

import dev.dreamers.ptd.helpers.InventoryHelper
import dev.dreamers.ptd.helpers.MessageHelper
import dev.dreamers.ptd.services.MessageService
import net.kyori.adventure.text.TextReplacementConfig
import org.bukkit.Material
import org.bukkit.block.Container
import org.bukkit.block.EnderChest
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class InteractListener : Listener {
    @EventHandler
    private fun onInteract(event: PlayerInteractEvent) {
        val player = event.player
        val block = event.clickedBlock ?: return

        if (
            event.action != Action.LEFT_CLICK_BLOCK ||
                !player.hasPermission("ptd.events.interact") ||
                player.inventory.itemInHand.type == Material.AIR ||
                block.state !is EnderChest && block.state !is Container
        )
            return

        val blockInventory = InventoryHelper.getInventory(player, block) ?: return
        val item = player.inventory.itemInMainHand
        val itemType = item.type
        var totalTransfer = 0

        if (player.isSneaking) {
            player.inventory.storageContents.forEachIndexed { index, slot ->
                if (slot?.type == itemType) {
                    val transferred =
                        InventoryHelper.transferItems(blockInventory, itemType, slot.amount)
                    totalTransfer += transferred

                    if (transferred == slot.amount) {
                        player.inventory.setItem(
                            index,
                            null,
                        )
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
            MessageService.TRANSFER_SUCCESS.replaceText(
                    TextReplacementConfig.builder()
                        .matchLiteral("%amount")
                        .replacement("$totalTransfer")
                        .build()
                )
                .replaceText(
                    TextReplacementConfig.builder()
                        .matchLiteral("%item")
                        .replacement(MessageHelper.formatString(itemType.name))
                        .build()
                )
                .replaceText(
                    TextReplacementConfig.builder()
                        .matchLiteral("%container")
                        .replacement(MessageHelper.formatString(block.type.name))
                        .build()
                ),
        )
    }
}
