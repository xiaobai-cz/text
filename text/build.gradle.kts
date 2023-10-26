@file:Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    `maven-publish`
}

android {
    namespace = "cc.xiaobaicz.widgets.text"
    compileSdk = 34

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.bundles.common)
    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.bundles.test.android)
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.github.xiaobaicz"
            artifactId = "text"
            version = "1.0.8"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}