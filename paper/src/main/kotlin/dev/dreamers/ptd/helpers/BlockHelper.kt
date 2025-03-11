package dev.dreamers.ptd.helpers

import de.marcely.bedwars.api.arena.Arena
import de.marcely.bedwars.tools.Helper
import dev.dreamers.ptd.services.ConfigService
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.inventory.InventoryHolder

class BlockHelper {
    companion object {
        fun isContainer(arena: Arena, block: Block): Boolean {
            if (arena.getChestType(block) != null) return true
            if (!ConfigService.INTERACTING && Helper.get().isInteractableBlock(block.type)) return false
            return block.state is InventoryHolder
        }
    }
}
