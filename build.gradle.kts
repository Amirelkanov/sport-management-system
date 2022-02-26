import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    id("org.jetbrains.compose") version "1.0.0"
}

group = "ru.senin.kotlin"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$embeddedKotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$embeddedKotlinVersion")
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.2.0")
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.16")

    runtimeOnly("org.slf4j:slf4j-simple:1.7.32")

    testImplementation("org.jetbrains.kotlin:kotlin-test:$embeddedKotlinVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$embeddedKotlinVersion")
    testImplementation("junit:junit:4.13.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Composed"
            packageVersion = "1.0.0"
        }
    }
}