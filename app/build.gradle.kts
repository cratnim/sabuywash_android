import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

val apiKeyProperties = Properties()
val apiKeyPropertiesFile = rootProject.file("api-key.properties")
if (apiKeyPropertiesFile.exists()) {
    apiKeyProperties.load(FileInputStream(apiKeyPropertiesFile))
}

android {
    namespace = "com.example.testintegrateui"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.sabuywash.sabuywashapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("profile") {
            initWith(getByName("debug"))
            isDebuggable = false
            matchingFallbacks.add("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    flavorDimensions += "env"
    
    productFlavors {
        create("local") {
            dimension = "env"
            applicationId = "com.sabuywash.sabuywashapp.local"
            // ใช้ ["key"] = value แทน .key = value
            manifestPlaceholders["googleApiKey"] = apiKeyProperties["LOCAL_GOOGLE_API_KEY"] as String
        }

        create("dev") {
            dimension = "env"
            applicationId = "com.sabuywash.sabuywashapp.development"
            manifestPlaceholders["googleApiKey"] = apiKeyProperties["DEV_GOOGLE_API_KEY"] as String
        }

        create("uat") {
            dimension = "env"
            applicationId = "com.sabuywash.sabuywashapp.uat"
            manifestPlaceholders["googleApiKey"] = apiKeyProperties["UAT_GOOGLE_API_KEY"] as String
        }

        create("prod") {
            dimension = "env"
            applicationId = "com.sabuywash.sabuywashapp"
            manifestPlaceholders["googleApiKey"] = apiKeyProperties["PROD_GOOGLE_API_KEY"] as String
        }
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.4")
    implementation(project(":flutter"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}