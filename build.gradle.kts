plugins {
    kotlin("jvm") version "1.7.21"
}

group = "kr.cosine.itemlink"
version = "1.0.0"

repositories {
    maven("https://maven.hqservice.kr/repository/maven-public/")
    maven("https://jitpack.io")
    mavenLocal()
}

dependencies {
    compileOnly("org.spigotmc", "spigot", "1.18.2-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl", "VaultAPI", "1.7")

    compileOnly("kr.hqservice", "hqframework-bukkit-core", "1.0.1-SNAPSHOT") { exclude("org.spigotmc", "spigot-api") }
    compileOnly("kr.hqservice", "hqframework-bukkit-inventory", "1.0.1-SNAPSHOT") { exclude("org.spigotmc", "spigot-api") }
    compileOnly("kr.hqservice", "hqframework-bukkit-command", "1.0.1-SNAPSHOT") { exclude("org.spigotmc", "spigot-api") }
    compileOnly("kr.hqservice", "hqframework-bukkit-nms", "1.0.1-SNAPSHOT") { exclude("org.spigotmc", "spigot-api") }

    testImplementation(kotlin("test"))
    testImplementation(kotlin("reflect"))
}

tasks {
    test {
        useJUnitPlatform()
    }
    jar {
        archiveFileName.set("${rootProject.name}-${rootProject.version}.jar")
        destinationDirectory.set(file("D:\\서버\\1.19.4 - 개발\\plugins"))
    }
}