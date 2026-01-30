pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()

        maven {
            url = uri("https://storage.googleapis.com/download.flutter.io")
        }
    }
}

rootProject.name = "testIntegrateUI"
include(":app")

val flutterProjectRoot = File(settingsDir.parentFile, "wash-app")
val includeFlutterScript = File(flutterProjectRoot, ".android/include_flutter.groovy")

if (includeFlutterScript.exists()) {
    apply(from = includeFlutterScript)
} else {
    throw GradleException("Flutter Not Found: ${includeFlutterScript.absolutePath}")
}