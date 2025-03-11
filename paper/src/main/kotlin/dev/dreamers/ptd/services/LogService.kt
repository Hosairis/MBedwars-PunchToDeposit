package dev.dreamers.ptd.services

import dev.dreamers.ptd.PunchToDeposit

class LogService {
    companion object {
        @JvmStatic
        fun info(input: String) {
            PunchToDeposit.getInst().logger.info(input)
        }

        @JvmStatic
        fun warning(input: String) {
            PunchToDeposit.getInst().logger.warning(input)
        }

        @JvmStatic
        fun severe(input: String) {
            PunchToDeposit.getInst().logger.severe(input)
        }

        @JvmStatic
        fun debug(input: String) {
            if (!ConfigService.DEBUG) return
            PunchToDeposit.getInst().logger.info("[DEBUG] $input")
        }
    }
}
