import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

val localProperties = Properties()
val localPropertiesFile: File? = rootProject.file("local.properties")
if (localPropertiesFile?.exists() == true) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}

android {
    namespace = "com.robertcoding.data"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        val apiKey = localProperties.getProperty("API_KEY")
        buildConfigField("String", "API_KEY", "\"$apiKey\"")

    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":common"))

    implementation(libs.androidx.paging.common)

    implementation(libs.ktor.common)
    implementation(libs.ktor.content.negotiation)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.logging)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.common)
    implementation(libs.koin.android)

    //Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.core.ktx)
    // Serialization
    implementation(libs.kotlin.serialization.json)

    //DataStore
    implementation(libs.androidx.datastore.core)
    implementation(libs.androidx.datastore.preferences)
    testImplementation(libs.junit)

}