package dev.dreamers.ptd

import de.marcely.bedwars.api.BedwarsAddon
import dev.dreamers.ptd.modules.deposit.DepositModule
import dev.dreamers.ptd.modules.holo.HoloModule
import dev.dreamers.ptd.modules.update.UpdateModule

class PunchToDepositAddon constructor(plugin: PunchToDeposit): BedwarsAddon(plugin) {
    fun registerModules() {
        HoloModule.register()
        DepositModule.register()
        UpdateModule.register()
    }

    fun unregisterModules() {
        UpdateModule.unregister()
    }
}
