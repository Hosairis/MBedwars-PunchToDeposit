package me.hosairis.ptd.modules.deposit.listeners

import de.marcely.bedwars.api.BedwarsAPI
import de.marcely.bedwars.api.arena.ArenaStatus
import de.marcely.bedwars.api.event.player.PlayerOpenArenaChestEvent
import de.marcely.bedwars.api.event.player.PlayerOpenArenaChestEvent.ChestType
import de.marcely.bedwars.tools.Helper
import me.hosairis.ptd.api.events.PostItemDepositEvent
import me.hosairis.ptd.api.events.PreItemDepositEvent
import me.hosairis.ptd.helpers.BlockHelper
import me.hosairis.ptd.helpers.InventoryHelper
import me.hosairis.ptd.helpers.MessageHelper
import me.hosairis.ptd.modules.deposit.DepositModule
import me.hosairis.ptd.modules.deposit.tasks.PlayerRemoval
import me.hosairis.ptd.modules.holo.HoloModule
import me.hosairis.ptd.services.ConfigService
import me.hosairis.ptd.services.LogService
import me.hosairis.ptd.services.MessageService
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class InteractListener : Listener {
    @EventHandler
    private fun onInteract(event: PlayerInteractEvent) {
        // Early returns for basic validation
        if (event.isCancelled || event.action != Action.LEFT_CLICK_BLOCK) return

        val player = event.player.takeIf { it.hasPermission("ptd.events.deposit") } ?: return
        val arena = BedwarsAPI.getGameAPI().getArenaByPlayer(player)
            ?.takeIf { it.status == ArenaStatus.RUNNING && player !in it.spectators }
            ?: return
        val clickedBlock = event.clickedBlock
            ?.takeIf { BlockHelper.isContainer(arena, it) && arena.isInside(it.location) }
            ?: return
        val chestType = arena.getChestType(clickedBlock) ?: return

        // Handle item validation
        val heldItem = player.itemInHand
            .takeIf { it.type != Material.AIR && it.type !in ConfigService.BLACKLISTED_ITEMS }
            ?: run {
                if (player.itemInHand.type in ConfigService.BLACKLISTED_ITEMS)
                    MessageHelper.sendMessage(player, MessageService.ITEM_BLACKLISTED)
                return
            }

        val blockInventory = InventoryHelper.getInventory(arena, player, clickedBlock) ?: return

        // Handle double-click mechanic
        if (ConfigService.DOUBLE_CLICK_ENABLED && player !in DepositModule.doubleClickPlayers) {
            DepositModule.doubleClickPlayers[player] = PlayerRemoval.schedulePlayerRemoval(player)
            LogService.debug("DC | ${player.name} Added")
            return
        }

        // Calculate transfer amount
        val availableSpace = InventoryHelper.getAvailableSpace(blockInventory, heldItem.type)
            .takeIf { it > 0 } ?: return
        val amountToTransfer = minOf(
            if (player.isSneaking) {
                InventoryHelper.getTotalItemAmount(player.inventory, heldItem.type)
            } else {
                heldItem.amount
            },
            availableSpace
        )

        // Fire pre-deposit event
        PreItemDepositEvent(player, heldItem, amountToTransfer).apply {
            Bukkit.getPluginManager().callEvent(this)
            if (isCancelled) return
        }

        // Fire chest open event
        PlayerOpenArenaChestEvent(
            player,
            arena,
            arena.getPlayerTeam(player),
            blockInventory,
            clickedBlock,
            chestType,
            PlayerOpenArenaChestEvent.OpenPurpose.PUNCH_TO_DEPOSIT
        ).apply {
            Bukkit.getPluginManager().callEvent(this)
            if (isCancelled) return
        }

        // Handle item transfer
        if (player.isSneaking) {
            val transferred = InventoryHelper.addAllItemsToInventory(blockInventory, player.inventory, heldItem)
            Helper.get().takeItems(player, heldItem, transferred)
        } else {
            val transferred = InventoryHelper.addItemsToInventory(blockInventory, heldItem, heldItem.amount)
            player.setItemInHand(if (transferred == heldItem.amount) null else heldItem.apply { amount -= transferred })
        }

        // Play sound based on chest type
        val soundName = when (chestType) {
            ChestType.PRIVATE -> ConfigService.PRIVATECHEST_SOUND
            else -> ConfigService.TEAMCHEST_SOUND
        }
        if (soundName.isNotEmpty() && !soundName.equals("None", true)) {
            Helper.get().playSound(clickedBlock.location, Helper.get().getSoundByName(soundName), 1f, 1f)
        }

        // Handle holograms
        if (ConfigService.HOLOGRAMS_REMOVE_AFTER_DEPOSIT) {
            HoloModule.holoList.firstOrNull {
                it.arena == arena && it.team == arena.getPlayerTeam(player)
            }?.removePlayer(player, if (!ConfigService.HOLOGRAMS_REMOVE_BOTH) chestType else null)
        }

        // Clean up double-click tracking
        if (ConfigService.DOUBLE_CLICK_ENABLED) {
            DepositModule.doubleClickPlayers[player]?.cancel()
            DepositModule.doubleClickPlayers.remove(player)
            LogService.debug("DC | ${player.name} Removed: Processed")
        }

        // Send transfer message
        val message = (if (chestType == ChestType.PRIVATE)
            MessageService.PRIVATECHEST_TRANSFER
        else
            MessageService.TEAMCHEST_TRANSFER)
            .replace("%amount", "$amountToTransfer")
            .replace("%item", MessageHelper.formatString(heldItem.type.name))
            .replace("%container", MessageHelper.formatString(clickedBlock.type.name))

        MessageHelper.sendMessage(player, message)

        // Fire post-deposit event
        PostItemDepositEvent(player, heldItem, amountToTransfer).apply {
            Bukkit.getPluginManager().callEvent(this)
        }
    }
}