plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "github.projectgroup.receptoriaApp"
    compileSdk = 35

    defaultConfig {
        applicationId = "github.projectgroup.receptoriaApp"
        minSdk = 24
        targetSdk = 35
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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
        // ======================================================
        // 1) Core & Lifecycle & Activity
        // ======================================================
        implementation("androidx.core:core-ktx:1.12.0")
        implementation("androidx.appcompat:appcompat:1.7.0")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
        implementation("androidx.activity:activity-compose:1.8.0")

        // ======================================================
        // 2) Compose UI & Tooling
        // ======================================================
        implementation("androidx.compose.ui:ui:1.5.0")
        implementation("androidx.compose.ui:ui-tooling-preview:1.5.0")
        debugImplementation("androidx.compose.ui:ui-tooling:1.5.0")
        debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.0")

        // ======================================================
        // 3) Material 3 (NavigationBar, Material3-компоненты и т.д.)
        // ======================================================
        implementation("androidx.compose.material3:material3:1.2.0")
        // Если вам нужны дополнительные утилиты из Material3, можно добавить:
        // implementation("androidx.compose.material3:material3-window-size-class:1.2.0")

        // ======================================================
        // 4) Material 2 (Scaffold, TopAppBar, TextField и т.д.)
        // ======================================================
        implementation("androidx.compose.material:material:1.5.0")
        implementation("androidx.compose.material:material-icons-core:1.5.0")
        implementation("androidx.compose.material:material-icons-extended:1.5.0")

        // ======================================================
        // 5) Navigation for Compose
        // ======================================================
        implementation("androidx.navigation:navigation-compose:2.7.0")

        // ======================================================
        // 6) Unit-тесты (JVM JUnit)
        // ======================================================
        testImplementation("junit:junit:4.13.2")

        // ======================================================
        // 7) Инструментальные androidTest
        // ======================================================
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.ext:junit-ktx:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
        androidTestImplementation("androidx.test:runner:1.5.2")
        androidTestImplementation("androidx.test:rules:1.5.0")
}