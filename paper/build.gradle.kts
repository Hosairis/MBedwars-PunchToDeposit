plugins {
    alias(libs.plugins.paperweight) // Paperweight plugin for Minecraft server development
    alias(libs.plugins.bukkit.factory) // Bukkit resource factory plugin for generating plugin.yml at build time
    alias(libs.plugins.run.paper) // The run-task plugin for running a test server and testing the plugin
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.marcely.de/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper) // Paper development bundle

    implementation(libs.bstats)
    implementation(libs.boosted.yaml)
    implementation(libs.minimessage)
    compileOnly(libs.mbedwars)
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(17) // Use Java 17 toolchain
}

tasks {
    runServer {
        minecraftVersion("1.18.2") // Configure the Minecraft server version.
        downloadPlugins {
            modrinth("viaversion", "5.2.2-SNAPSHOT+672")
            modrinth("viabackwards", "5.2.2-SNAPSHOT+386")
        }
    }

    shadowJar {
        relocate("org.bstats", "dev.dreamers.ptd.libs.bstats")
        relocate("dev.dejvokep.boostedyaml", "dev.dreamers.ptd.libs.boostedyaml")
    }
}

bukkitPluginYaml {
    name = "PunchToDeposit"
    main = "dev.dreamers.ptd.PunchToDeposit"
    authors.add("Rafi")
    apiVersion = "1.13"
    softDepend.add("MBedwars")
}
