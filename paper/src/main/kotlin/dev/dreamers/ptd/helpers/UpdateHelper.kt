package dev.dreamers.ptd.helpers

import com.google.gson.Gson
import dev.dreamers.ptd.PunchToDeposit
import dev.dreamers.ptd.services.ConfigService
import dev.dreamers.ptd.services.LogService
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask

class UpdateHelper {
    companion object {
        const val PLUGIN_NAME = "PunchToDeposit"
        const val PLUGIN_VERSION = "1.0.0"

        @Volatile
        var isOutDated: Boolean = false
            private set

        private var task: BukkitTask? = null

        private val httpClient: HttpClient = HttpClient.newHttpClient()
        private val httpRequest: HttpRequest =
            HttpRequest.newBuilder()
                .uri(URI.create("https://dreamers.dev/apps/versions.php?name=PunchToDeposit"))
                .GET()
                .build()

        data class ApiResponse(val ok: Boolean, val data: Data, val message: String)

        data class Data(val name: String, val version: String)

        private fun checkForUpdates() {
            task?.cancel()
            if (!ConfigService.UPDATE_CHECK_ENABLED) return

            task =
                Bukkit.getScheduler()
                    .runTaskTimerAsynchronously(
                        PunchToDeposit.getInst(),
                        Runnable {
                            httpClient
                                .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                                .thenApply { response ->
                                    try {
                                        Gson().fromJson(response.body(), ApiResponse::class.java)
                                    } catch (e: Exception) {
                                        null
                                    }
                                }
                                .thenAccept { jsonData ->
                                    if (
                                        jsonData == null ||
                                            !jsonData.ok ||
                                            jsonData.data.name != PLUGIN_NAME
                                    )
                                        return@thenAccept

                                    isOutDated = jsonData.data.version != PLUGIN_VERSION
                                }
                        },
                        36000,
                        100,
                    )
            LogService.info("Started update check")
        }

        fun startUpdateCheck() {
            checkForUpdates()
        }

        fun stopUpdateCheck() {
            task?.cancel()
        }
    }
}
