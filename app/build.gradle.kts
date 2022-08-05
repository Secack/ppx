import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import de.fayard.refreshVersions.core.versionFor
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.ByteArrayOutputStream
import java.util.*

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.devtools.ksp")
}

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

val verCode = 1991
val verName = "1.9.9.1"

android {
    compileSdk = 32

    namespace = "com.akari.ppx"

    defaultConfig {
        applicationId = "com.akari.ppx"
        minSdk = 23

        targetSdk = 32

        versionCode = verCode
        versionName = verName

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    val config = properties.getProperty("storeFile")?.let {
        signingConfigs.create("config") {
            storeFile = file(it)
            storePassword = properties.getProperty("storePassword")
            keyAlias = properties.getProperty("keyAlias")
            keyPassword = properties.getProperty("keyPassword")
        }
    }

    buildTypes {
        all {
            signingConfig = config ?: signingConfigs["debug"]
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = versionFor(AndroidX.compose.ui)
    }
    packagingOptions {
        resources {
            excludes += arrayOf("META-INF/**", "kotlin/**", "google/**", "**.bin")
        }
    }
}

kotlin {
    sourceSets {
        debug {
            kotlin.srcDir("build/generated/ksp/debug/kotlin")
        }
        release {
            kotlin.srcDir("build/generated/ksp/release/kotlin")
        }
    }
}

android.applicationVariants.all {
    outputs.all {
        (this as BaseVariantOutputImpl).outputFileName =
            "皮皮虾助手 ${versionName}.apk"
    }
}

dependencies {
    compileOnly(Libs.xposed_api)
    api(Libs.gson)
    implementation(AndroidX.dataStore.preferences)
    implementation(AndroidX.compose.material)
    implementation(AndroidX.compose.ui.tooling)
    implementation(AndroidX.appCompat)
    implementation(AndroidX.activity.compose)
    implementation(Google.android.material)
    implementation(Google.accompanist.insets)
    implementation(Google.accompanist.pager)
    implementation(Google.accompanist.pager.indicators)
    implementation(Libs.reorderable)
    implementation(Libs.mp4parser)
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
            "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
            "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
            "-opt-in=com.google.accompanist.pager.ExperimentalPagerApi",
            "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi"
        )

        if (name.contains("release", true)) {
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-Xassertions=always-disable",
                "-Xno-param-assertions",
                "-Xno-call-assertions",
                "-Xno-receiver-assertions",
                "-opt-in=kotlin.RequiresOptIn"
            )
        }
    }
}