import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.compose") version "1.0.1"
}

group = "com.tuesd4y"
version = "1.0"

repositories {
    maven("https://repo.osgeo.org/repository/release/")
    google()
    mavenCentral {
        content {
            excludeModule("javax.media", "jai_core")
        }
    }
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://maven.geo-solutions.it/")
}

dependencies {
    implementation(compose.desktop.currentOs)

    implementation("org.pushing-pixels:aurora-theming:1.0.1")
    implementation("org.pushing-pixels:aurora-component:1.0.1")
    implementation("org.pushing-pixels:aurora-window:1.0.1")

    implementation("com.squareup.moshi:moshi-kotlin:1.13.0")

    implementation("org.geotools:gt-main:26.1.06")
    implementation("org.geotools:gt-swing:26.1.06")
    implementation("org.geotools:gt-shapefile:26.1.06")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "google-takeout-viewer"
            packageVersion = "1.0.0"
        }
    }
}