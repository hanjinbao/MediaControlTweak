@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.internal.api.BaseVariantOutputImpl

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.rcmiku.media.control.tweak"
    compileSdk = 35
    defaultConfig {
        applicationId = namespace
        minSdk = 34
        targetSdk = 35
        versionCode = 1400
        versionName = "1.4.0"
    }
    signingConfigs {
        release {
            // 检查是否在 CI 环境中运行（GitHub Actions）
            if (System.getenv("CI")) {
                // 这些将由 GitHub Actions 工作流提供
                storeFile = file("../keystore.jks")
                storePassword = System.getenv("KEY_STORE_PASSWORD")
                keyAlias = System.getenv("KEY_ALIAS")
                keyPassword = System.getenv("KEY_PASSWORD")
            } else {
                // 本地开发时，可以在 local.properties 中设置这些属性
                // 或者在不进行本地发布构建时注释掉此部分
                storeFile = null
                storePassword = ""
                keyAlias = ""
                keyPassword = ""
            }
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.findByName("release")
        }
        debug {
            versionNameSuffix = "-debug"
            applicationIdSuffix = ".debug"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    buildToolsVersion = "35.0.0"
    dependenciesInfo {
        includeInBundle = false
        includeInApk = false
    }
    packaging {
        resources.excludes += "**"
        applicationVariants.all {
            outputs.all {
                (this as BaseVariantOutputImpl).outputFileName =
                    "media-control-tweak-$versionName.apk"
            }
        }
    }
}

dependencies {
    compileOnly(libs.xposed)
    implementation(libs.ezXHelper)
}
