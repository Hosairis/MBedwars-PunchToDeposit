package dev.dreamers.ptd.helpers

import org.bukkit.Bukkit

class VersionHelper private constructor() {
    companion object {
        private val serverVersion: Version by lazy { detectVersion() }

        @JvmStatic fun getCurrentVersion(): Version = serverVersion

        @JvmStatic fun isNewerThan(version: Version): Boolean = serverVersion.id > version.id

        @JvmStatic fun isOlderThan(version: Version): Boolean = serverVersion.id < version.id

        @JvmStatic fun isVersion(version: Version): Boolean = serverVersion.id == version.id

        private fun detectVersion(): Version {
            val bukkitVersion = Bukkit.getBukkitVersion()
            val versionString = bukkitVersion.substringBefore('-')
            return Version.fromString(versionString)
        }
    }

    enum class Version(val id: Int) {
        V1_8(8),
        V1_9(9),
        V1_10(10),
        V1_11(11),
        V1_12(12),
        V1_13(13),
        V1_14(14),
        V1_15(15),
        V1_16(16),
        V1_17(17),
        V1_18(18),
        V1_19(19),
        V1_20(20),
        V1_21(21),
        UNKNOWN(-1);

        companion object {
            fun fromString(version: String): Version {
                val version = version.substringBeforeLast('.')
                val normalizedVersion = "V${version.replace(".", "_")}"
                return try {
                    valueOf(normalizedVersion)
                } catch (e: IllegalArgumentException) {
                    UNKNOWN
                }
            }
        }
    }
}
