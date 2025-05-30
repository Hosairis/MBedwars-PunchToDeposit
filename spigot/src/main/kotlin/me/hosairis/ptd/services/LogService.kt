package me.hosairis.ptd.services

import me.hosairis.ptd.MBPunchToDeposit

class LogService {
    companion object {
        @JvmStatic
        fun info(input: String) {
            MBPunchToDeposit.getInst().logger.info(input)
        }

        @JvmStatic
        fun warning(input: String) {
            MBPunchToDeposit.getInst().logger.warning(input)
        }

        @JvmStatic
        fun severe(input: String) {
            MBPunchToDeposit.getInst().logger.severe(input)
        }

        @JvmStatic
        fun debug(input: String) {
            if (!ConfigService.DEBUG) return
            MBPunchToDeposit.getInst().logger.info("[DEBUG] $input")
        }
    }
}
