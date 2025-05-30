package me.hosairis.ptd

import de.marcely.bedwars.api.BedwarsAddon
import me.hosairis.ptd.modules.deposit.DepositModule
import me.hosairis.ptd.modules.holo.HoloModule
import me.hosairis.ptd.modules.update.UpdateModule

class MBPunchToDepositAddon constructor(plugin: MBPunchToDeposit): BedwarsAddon(plugin) {
    fun registerModules() {
        HoloModule.register()
        DepositModule.register()
        UpdateModule.register()
    }

    fun unregisterModules() {
        UpdateModule.unregister()
    }

    override fun getName(): String {
        return MBPunchToDeposit.PLUGIN_NAME
    }
}
