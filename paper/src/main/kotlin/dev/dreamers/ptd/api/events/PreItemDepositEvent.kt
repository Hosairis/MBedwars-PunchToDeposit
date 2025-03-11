package dev.dreamers.ptd.api.events

import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.inventory.ItemStack

class PreItemDepositEvent(
    val player: Player,
    val item: ItemStack,
    var amount: Int
) : Event(), Cancellable {
    private var cancelled = false

    override fun isCancelled() = cancelled
    override fun setCancelled(cancel: Boolean) {
        cancelled = cancel
    }

    override fun getHandlers() = HANDLERS

    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList() = HANDLERS
    }
}
