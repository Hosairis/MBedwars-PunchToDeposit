package dev.dreamers.ptd.modules.update.tasks

import com.google.gson.Gson
import dev.dreamers.ptd.PunchToDeposit
import dev.dreamers.ptd.modules.update.ApiData
import dev.dreamers.ptd.modules.update.UpdateModule
import dev.dreamers.ptd.services.LogService
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class UpdateCheck {
    companion object {
        fun checkForUpdates(): BukkitTask {
            return object : BukkitRunnable() {
                override fun run() {
                    LogService.debug("Checking for updates")
                    try {
                        val url = URL("https://dreamers.dev/apps/versions.php?name=${PunchToDeposit.PLUGIN_NAME}")
                        val connection = url.openConnection() as HttpURLConnection
                        connection.requestMethod = "GET"

                        val responseCode = connection.responseCode
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            val reader = BufferedReader(InputStreamReader(connection.inputStream))
                            val response = reader.use { it.readText() }
                            reader.close()

                            val jsonData = try {
                                Gson().fromJson(response, ApiData::class.java)
                            } catch (e: Exception) {
                                null
                            }

                            if (jsonData == null || !jsonData.ok || jsonData.data.name != PunchToDeposit.PLUGIN_NAME) {
                                return
                            }

                            LogService.debug("V: ${jsonData.data.version}")
                            UpdateModule.isOutDated = jsonData.data.version != PunchToDeposit.PLUGIN_VERSION
                        }
                    } catch (e: Exception) {
                        LogService.debug("Update check failed: ${e.message}")
                    }
                }
            }.runTaskTimerAsynchronously(PunchToDeposit.getInst(), 0, 12000)
        }
    }
}