plugins {
    kotlin("jvm") version "2.3.0"
    id("maven-publish")
}

repositories {
    mavenCentral()
}

dependencies {
    api("ch.qos.logback:logback-core:1.5.22")
    api("ch.qos.logback:logback-classic:1.5.22")

    // Tests
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:6.0.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.yonatankarp"
            artifactId = project.name
            version = project.version.toString()

            from(components["java"])
        }

        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/yonatankarp/kotlin-junit-tools")
                credentials {
                    username = project.findProperty("gpr.user")?.toString() ?: System.getenv("GITHUB_ACTOR")
                    password = project.findProperty("gpr.key")?.toString() ?: System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
}
