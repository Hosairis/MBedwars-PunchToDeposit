package dev.dreamers.ptd.modules.update.tasks

import com.google.gson.Gson
import dev.dreamers.ptd.PunchToDeposit
import dev.dreamers.ptd.modules.update.ApiData
import dev.dreamers.ptd.modules.update.UpdateModule
import dev.dreamers.ptd.services.LogService
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class UpdateCheck {
    companion object {
        private val httpClient: HttpClient = HttpClient.newHttpClient()
        private val httpRequest: HttpRequest =
            HttpRequest.newBuilder()
                .uri(URI.create("https://dreamers.dev/apps/versions.php?name=PunchToDeposit"))
                .GET()
                .build()

        fun checkForUpdates(): BukkitTask {
            return object : BukkitRunnable() {
                override fun run() {
                    LogService.debug("Checking for updates")
                    httpClient
                        .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                        .thenApply { response ->
                            try {
                                Gson().fromJson(response.body(), ApiData::class.java)
                            } catch (e: Exception) {
                                null
                            }
                        }
                        .thenAccept { jsonData ->
                            if (
                                jsonData == null ||
                                !jsonData.ok ||
                                jsonData.data.name != PunchToDeposit.PLUGIN_NAME
                            )
                                return@thenAccept

                            LogService.debug("V: ${jsonData.data.version}")

                            UpdateModule.isOutDated = jsonData.data.version != PunchToDeposit.PLUGIN_VERSION
                        }
                }
            }.runTaskTimerAsynchronously(PunchToDeposit.getInst(), 0, 6000)
        }
    }
}