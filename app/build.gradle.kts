import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.google.hilt)
}

android {
    namespace = "band.effective.hdl"
    compileSdk = 34

    val authClientSecret = gradleLocalProperties(project.rootDir, providers).getProperty("authClientSecret")
    val authClientId = gradleLocalProperties(project.rootDir, providers).getProperty("authClientId")

    defaultConfig {
        applicationId = "band.effective.hdl"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        buildConfigField(
            "String",
            "AUTH_REDIRECT_URL",
            "\"band.effective.hdl.redirect\""
        )
        buildConfigField(
            "String",
            "AUTH_SERVER_URL",
            "\"https://leader-id.ru/apps/authorize\""
        )
        buildConfigField("String", "AUTH_CLIENT_SECRET", "\"$authClientSecret\"")
        buildConfigField("String", "AUTH_CLIENT_ID", "\"$authClientId\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        manifestPlaceholders["appAuthRedirectScheme"] = "band.effective.hdl.redirect"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildFeatures{
        buildConfig = true
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
    implementation(libs.androidx.datastore.core)
    implementation(libs.androidx.datastore.preferences)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.coil.compose)

    implementation(libs.openid.appauth)

    implementation(libs.retrofit.mock)
    implementation(libs.retrofit.moshi)
    implementation(libs.retrofit.client)
    implementation(libs.retrofit.scalars)
    implementation(libs.okhttp.logging)

    implementation(libs.moshi.kotlin)
    implementation(libs.moshi.adapters)
    ksp(libs.moshi.compiler)

    implementation(libs.google.dagger.hilt)
    ksp(libs.google.dagger.compiler)

    implementation(libs.androidx.navigation)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.retrofit.adapters)

    implementation(libs.google.vebview)
}