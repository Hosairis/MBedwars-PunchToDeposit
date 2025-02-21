package dev.dreamers.ptd.helpers

import dev.dejvokep.boostedyaml.YamlDocument
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings
import dev.dreamers.ptd.PunchToDeposit
import java.io.File

abstract class StorageHelper(private val fileName: String) {

    private lateinit var configuration: YamlDocument

    fun init() {
        val inputStream =
            {}.javaClass.getResourceAsStream("/$fileName")
                ?: throw RuntimeException("$fileName is missing from the jar!")

        configuration =
            YamlDocument.create(
                File(PunchToDeposit.getAddon().dataFolder, fileName),
                inputStream,
                GeneralSettings.DEFAULT,
                DumperSettings.DEFAULT,
                LoaderSettings.builder().setAutoUpdate(true).build(),
                UpdaterSettings.builder().setVersioning(BasicVersioning("config-version")).build(),
            )

        loadValues()
    }

    protected abstract fun loadValues()

    fun reload(): Boolean = configuration.reload().also { if (it) loadValues() }

    protected fun getConfig(): YamlDocument = configuration
}
