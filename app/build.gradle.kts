plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.clearcounts"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.clearcounts"
        minSdk = 25
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

    // Hilt - Implementaciones
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Room - Implementaciones
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    // Room
    ksp(libs.room.ksp)

    // Requerida para usar Theme.Material3 en XML
    implementation("com.google.android.material:material:1.11.0") // Usar la última versión estable

    // Requerida para usar temas de compatibilidad de AndroidX
    implementation("androidx.appcompat:appcompat:1.6.1") // Usar la última versión estable

    // Si estás usando la librería de Splash Screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // ... dependencias de Compose (material3)
    implementation("androidx.compose.material3:material3")

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.core)


   //ggg
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.material3)
    implementation(libs.androidx.compose.material3)
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation(libs.androidx.runtime)
    implementation(libs.androidx.ui.text)
    implementation(libs.androidx.compose.animation.core)
    implementation(libs.androidx.compose.ui.text)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3.material3)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.hilt.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.navigation:navigation-compose:2.9.5")
}