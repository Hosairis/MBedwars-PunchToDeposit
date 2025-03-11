package dev.dreamers.ptd.api.events

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.inventory.ItemStack

class PostItemDepositEvent(
    val player: Player,
    val item: ItemStack,
    val amount: Int
) : Event() {

    override fun getHandlers() = HANDLERS

    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList() = HANDLERS
    }
}
