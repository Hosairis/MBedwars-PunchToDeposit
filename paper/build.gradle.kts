plugins {
    alias(libs.plugins.paperweight) // Paperweight plugin for Minecraft server development
    alias(libs.plugins.bukkit.factory) // Bukkit resource factory plugin for generating plugin.yml at build time
    alias(libs.plugins.run.paper) // The run-task plugin for running a test server and testing the plugin
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper) // Paper development bundle
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(17) // Use Java 17 toolchain
}

tasks {
    runServer { minecraftVersion("1.18.2") } // Configure the Minecraft server version.
}

bukkitPluginYaml {
    name = "MCTemplate"
    main = "example.plugin.template.Template"
    authors.add("Author")
    apiVersion = "1.18"
}
