/*
 * Copyright (c) 2024 EverNet.
 * Tutti i diritti riservati.
 * Progetto: EverCraft - https://evercraft.it/
 * È vietata la riproduzione, distribuzione, trasmissione o modifica di questo contenuto
 * in qualsiasi forma o con qualsiasi mezzo senza l'autorizzazione scritta esplicita di EverNet.
 * Ogni utilizzo non autorizzato di questo materiale, compresa la creazione di opere derivate o copie,
 * sarà perseguito ai sensi della legge vigente.
 */


import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.internal.classpath.Instrumented.systemProperty


plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.shadowJar)
}

group = "it.evercraft"
version = "1.8-1.21.1"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        content {
            includeGroup("org.bukkit")
            includeGroup("org.spigotmc")
        }
    }
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/central") }
    maven { url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/") }
    maven("https://nexus.unstackss.dev/repository/maven-releases/") {
        name = "unstackss-repo"
    }
}

dependencies {
    compileOnly("dev.unstackss:EverSpigotAPI:1.21.1:api21R2")
    implementation(libs.kyori)
    implementation(libs.iridium)
    implementation(libs.md5)
    implementation(libs.snakeyaml)
    implementation(libs.annotations) {
        exclude(group = "org.yaml", module = "snakeyaml")
    }
    implementation(kotlin("stdlib:1.7.21"))
    implementation(libs.kotlinxCoroutinesCore) {
        exclude(group = "org.yaml", module = "snakeyaml")
    }
    implementation(libs.bstatsBukkit) {
        exclude(group = "org.yaml", module = "snakeyaml")
    }
    compileOnly(libs.lombok)
}

val targetJavaVersion = 20
java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
}


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    if (targetJavaVersion >= 17 || JavaVersion.current().isJava10Compatible) {
        kotlinOptions.jvmTarget = targetJavaVersion.toString()
    }

}



tasks {

    val shadowJar = named<ShadowJar>("shadowJar")

    shadowJar.configure {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        archiveBaseName.set("EverBorder")
        relocate("org.bstats", "it.evercraft.everborder.bstats")
        relocate("com.iridium", "it.evercraft.everborder.iridium")

        listOf(
            "org.bstats"
        ).forEach {
            relocate(it, "libs.$it")
        }
        listOf("com.iridium").forEach {
            relocate(it, "libs.$it")
        }

        exclude("META-INF/**")
        exclude("com/google/**")
        exclude("mojang-translations/**")
        exclude("org/apache/**")
        exclude("org/joml/**")
        exclude("org/json/**")
        exclude("_COROUTINE/**")
        exclude("org/checkerframework/**")
        exclude("DebugProbesKt.bin")

        mergeServiceFiles()

        from("src/main/resources") {
            include("**/*")
        }

        destinationDirectory.set(file("target"))

        archiveFileName.set("EverBorder.jar")

        doFirst {
            val hexaEconAsciiArt = """
    ______                ____                 __         
   / ____/   _____  _____/ __ )____  _________/ /__  _____
  / __/ | | / / _ \/ ___/ __  / __ \/ ___/ __  / _ \/ ___/
 / /___ | |/ /  __/ /  / /_/ / /_/ / /  / /_/ /  __/ /    
/_____/ |___/\___/_/  /_____/\____/_/   \__,_/\___/_/     
            """.trimIndent()

            println(hexaEconAsciiArt)

            logger.quiet("""
            Collecting PlaceHolders....
            Building PlaceHolders...
            
            Building EverGens....
            """.trimIndent())
        }

        logging.captureStandardOutput(LogLevel.LIFECYCLE)
        outputs.upToDateWhen { false }
    }

    named("build") {
        dependsOn(shadowJar)
    }


    kotlin {
        sourceSets.all {
            languageSettings {
                languageVersion = "2.0"
            }
        }
        sourceSets {
            named("main") {
                kotlin.srcDir("src/main/kotlin")
            }
        }

        compilerOptions {
            freeCompilerArgs = listOf(
                "-Xuse-k2",
                "-Xno-call-assertions",
                "-Xopt-in=kotlin.ExperimentalStdlibApi"
            )
        }
    }
}

tasks.withType<ShadowJar>().configureEach {
    systemProperty("org.gradle.warning.mode", "none")
}