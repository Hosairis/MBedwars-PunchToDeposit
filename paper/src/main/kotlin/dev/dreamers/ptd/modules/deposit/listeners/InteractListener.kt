package dev.dreamers.ptd.modules.deposit.listeners

import de.marcely.bedwars.api.BedwarsAPI
import de.marcely.bedwars.api.arena.ArenaStatus
import de.marcely.bedwars.api.event.player.PlayerOpenArenaChestEvent
import de.marcely.bedwars.api.event.player.PlayerOpenArenaChestEvent.ChestType
import de.marcely.bedwars.tools.Helper
import dev.dreamers.ptd.api.events.PostItemDepositEvent
import dev.dreamers.ptd.api.events.PreItemDepositEvent
import dev.dreamers.ptd.helpers.BlockHelper
import dev.dreamers.ptd.helpers.InventoryHelper
import dev.dreamers.ptd.helpers.MessageHelper
import dev.dreamers.ptd.modules.holo.HoloModule
import dev.dreamers.ptd.services.ConfigService
import dev.dreamers.ptd.services.MessageService
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class InteractListener: Listener {
    @EventHandler
    private fun onInteract(event: PlayerInteractEvent) {
        if (event.isCancelled || event.action != Action.LEFT_CLICK_BLOCK) return

        val player = event.player
            .takeIf { it.hasPermission("ptd.events.interact") } ?: return
        val arena = BedwarsAPI.getGameAPI().getArenaByPlayer(player)
            ?.takeIf { it.status == ArenaStatus.RUNNING && player !in it.spectators } ?: return
        val clickedBlock = event.clickedBlock
            ?.takeIf { BlockHelper.isContainer(arena, it) && arena.isInside(it.location) } ?: return
        val chestType = arena.getChestType(clickedBlock) ?: return
        val item = player.itemInHand
            .takeIf { it.type != Material.AIR && it.type !in ConfigService.BLACKLISTED_ITEMS }
            ?: run {
                if (player.itemInHand.type in ConfigService.BLACKLISTED_ITEMS) { MessageHelper.sendMessage(player, MessageService.ITEM_BLACKLISTED) }
                return
            }
        val blockInventory = InventoryHelper.getInventory(arena, player, clickedBlock) ?: return
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
        PlayerOpenArenaChestEvent(player, arena, arena.getPlayerTeam(player), blockInventory, clickedBlock, chestType, PlayerOpenArenaChestEvent.OpenPurpose.PUNCH_TO_DEPOSIT).apply {
            Bukkit.getPluginManager().callEvent(this)
            if (isCancelled) return
        }

        if (player.isSneaking) {
            val transferred = InventoryHelper.addAllItemsToInventory(blockInventory, player.inventory, item)
            Helper.get().takeItems(player, item, transferred)
        } else {
            val transferred = InventoryHelper.addItemsToInventory(blockInventory, item, item.amount)
            player.setItemInHand(if (transferred == item.amount) null else item.apply { amount -= transferred })
        }

        val soundName = when (chestType) {
            ChestType.PRIVATE -> ConfigService.PRIVATECHEST_SOUND
            else -> ConfigService.TEAMCHEST_SOUND
        }
        if (soundName.isNotEmpty() && !soundName.equals("None", true)) {
            Helper.get().playSound(clickedBlock.location, Helper.get().getSoundByName(soundName), 1f, 1f)
        }
        if (ConfigService.HOLOGRAMS_REMOVE_AFTER_DEPOSIT) {
            val holo = HoloModule.holoList.firstOrNull { it.arena == arena && it.team == arena.getPlayerTeam(player) }
            holo?.removePlayer(player, chestType)
        }

        MessageHelper.sendMessage(
            player,
            (if (chestType == ChestType.PRIVATE)
                MessageService.PRIVATECHEST_TRANSFER
            else
                MessageService.TEAMCHEST_TRANSFER)
                .replace("%amount", "$amountToTransfer")
                .replace("%item", MessageHelper.formatString(item.type.name))
                .replace("%container", MessageHelper.formatString(clickedBlock.type.name)),
        )
        PostItemDepositEvent(player, item, amountToTransfer).apply {
            Bukkit.getPluginManager().callEvent(this)
        }
    }
}