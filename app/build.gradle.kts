plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.dagger.hilt)
    //id("kotlin-kapt")
}

android {
    namespace = "com.imtiaz.dictify"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.imtiaz.dictify"
        minSdk = 25
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        //buildConfigField("String", "API_KEY", "\"ebe00b316bmsh8190a541fa1249cp101471jsnfe67187c2328\"")
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
        buildConfig = true
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {

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

    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.androidx.window)
    implementation(libs.androidx.fragment.ktx)


    implementation(libs.play.services.location)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.lottie.compose)
    implementation(libs.accompanist.pager.v0301)
    implementation(libs.accompanist.pager.indicators.v0301)

    implementation(libs.coil.compose)
    implementation(libs.accompanist.insets)
    implementation(libs.accompanist.insets.ui)
    implementation(libs.androidx.foundation)

    //biometric
    implementation(libs.androidx.biometric)
    implementation(libs.androidx.datastore.preferences)
    //Constraint
    implementation(libs.androidx.constraintlayout.compose)
    //retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.gson)
    implementation(libs.logging.interceptor)
    //hilt
    implementation(libs.hilt.android)
    //kapt(libs.hilt.compiler)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    //biometric
    implementation(libs.androidx.biometric)
    //sharePreference
    implementation(libs.androidx.security.crypto)
    //camera
    implementation (libs.androidx.camera.core)
    implementation (libs.androidx.camera.camera2)
    implementation (libs.androidx.camera.lifecycle)
    implementation (libs.androidx.camera.view)
    implementation (libs.barcode.scanning)

    //google map
    implementation (libs.maps.compose)
    implementation (libs.play.services.maps)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.multidex)
    // Logger
    implementation(libs.timber)

    // Room database
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    annotationProcessor(libs.room.compiler)
}