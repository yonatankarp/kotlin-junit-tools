# Kotlin Junit Tools

A toolkit of helper classes in Kotlin that would help you while writing your
unit tests in jUnit.

To consume the library add the following to your `build.gradle.kts` script:

```kotlin
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/yonatankarp/kotlin-junit-tools")
        credentials {
            username = project.findProperty("gpr.user")?.toString() ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.key")?.toString() ?: System.getenv("GITHUB_TOKEN")
        }
    }
}
```

After refreshing you can consume the library as follows:

```kotlin
dependencies {
    implementation("com.yonatankarp:kotlin-junit-tools:0.1.0")
}
```
