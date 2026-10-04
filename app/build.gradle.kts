plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.tzvi.appguide"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.tzvi.appguide"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        release { isMinifyEnabled = false }
    }

    buildFeatures { compose = true }

    packaging.resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
}

kotlin { jvmToolchain(17) }

dependencies {
    implementation("androidx.activity:activity-compose:1.11.0")
    implementation("androidx.compose.material3:material3:1.3.2")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
}
