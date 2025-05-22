import xyz.jpenilla.resourcefactory.bukkit.Permission

plugins {
    alias(libs.plugins.bukkit.factory) // Bukkit resource factory plugin for generating plugin.yml at build time
    alias(libs.plugins.run.paper) // The run-task plugin for running a test server and testing the plugin
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.marcely.de/repository/maven-public/")
}

dependencies {
    compileOnly(libs.spigot)

    implementation(libs.bstats)
    implementation(libs.boosted.yaml)
    compileOnly(libs.mbedwars)
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(17) // Use Java 17 to compile
}

// Output Java 8-compatible bytecode for Java classes
tasks.withType<JavaCompile>().configureEach {
    options.release.set(8)
}

// Output Java 8-compatible bytecode for Java classes (For Kotlin)
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8)
    }
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
    authors.add("Hosairis")
    apiVersion = "1.13"
    depend.add("MBedwars")
    commands {
        create("ptd") {
            description = "Main command for PunchToDeposit"
            usage = "/ptd <subcommand>"
            aliases.addAll(listOf("punchtodeposit"))
        }
    }
    permissions {
        create("ptd.events.deposit") {
            default = Permission.Default.TRUE
        }
    }
}
