import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystorePropertiesFile.inputStream().use { keystoreProperties.load(it) }
}

android {
    namespace = "com.nftflicks.app"
    // Google Play: new apps/updates must target API 36 (Android 16) as of Aug 31, 2026.
    compileSdk = 36

    defaultConfig {
        applicationId = "com.nftflicks.app"
        minSdk = 26
        targetSdk = 36
        versionCode = 11
        versionName = "1.0.10"
        buildConfigField("String", "SITE_URL", "\"https://nftflicks.com/\"")
        buildConfigField("String", "API_HOST", "\"nftflicks.com\"")
        buildConfigField("String", "SITE_HOST", "\"nftflicks.com\"")
        // Google Cast Custom Receiver application ID (cast.google.com/publish).
        buildConfigField("String", "CAST_APP_ID", "\"6C87B84D\"")
    }

    buildFeatures {
        buildConfig = true
        viewBinding = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    signingConfigs {
        create("release") {
            val storeFilePath =
                keystoreProperties.getProperty("storeFile")
                    ?: System.getenv("NFTFLICKS_STORE_FILE")
            val storePassword =
                keystoreProperties.getProperty("storePassword")
                    ?: System.getenv("NFTFLICKS_STORE_PASSWORD")
            val keyAlias =
                keystoreProperties.getProperty("keyAlias")
                    ?: System.getenv("NFTFLICKS_KEY_ALIAS")
            val keyPassword =
                keystoreProperties.getProperty("keyPassword")
                    ?: System.getenv("NFTFLICKS_KEY_PASSWORD")
            if (!storeFilePath.isNullOrBlank() &&
                !storePassword.isNullOrBlank() &&
                !keyAlias.isNullOrBlank() &&
                !keyPassword.isNullOrBlank()
            ) {
                // Paths in keystore.properties are relative to the android-app root.
                storeFile = rootProject.file(storeFilePath)
                this.storePassword = storePassword
                this.keyAlias = keyAlias
                this.keyPassword = keyPassword
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            val useDebugSigning = (project.findProperty("USE_DEBUG_SIGNING") as String?) == "true"
            val releaseCfg = signingConfigs.getByName("release")
            signingConfig = when {
                useDebugSigning -> signingConfigs.getByName("debug")
                releaseCfg.storeFile != null -> releaseCfg
                else -> {
                    logger.warn(
                        "Release keystore not configured. Run tools/create-upload-keystore.ps1 " +
                            "or pass -PUSE_DEBUG_SIGNING=true for local testing only."
                    )
                    null
                }
            }
        }
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.webkit:webkit:1.12.1")
    implementation("androidx.activity:activity-ktx:1.9.3")
    implementation("androidx.browser:browser:1.8.0")
    implementation("androidx.mediarouter:mediarouter:1.7.0")
    implementation("com.google.android.gms:play-services-cast-framework:21.5.0")
}
