import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "com.atakanmadanoglu.movieapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.atakanmadanoglu.movieapp"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val apiKeyPropertiesFile = rootProject.file("local.properties")
        val apiKeyProperties = Properties()
        apiKeyProperties.load(apiKeyPropertiesFile.inputStream())

        buildConfigField("String", "API_KEY", apiKeyProperties.getProperty("API_KEY"))
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

    buildFeatures {
        viewBinding = true
        buildConfig = true
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
    val room_version = "2.5.2"
    val paging_version = "3.2.0"
    val hilt_version = "2.44.2"
    val retrofit_version = "2.9.0"
    val okhttp_version = "4.10.0"

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Hilt
    implementation("com.google.dagger:hilt-android:$hilt_version")
    kapt("com.google.dagger:hilt-android-compiler:$hilt_version")

    // Room
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    // To use Kotlin Symbol Processing (KSP)
    ksp("androidx.room:room-compiler:$room_version")
    // Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")
    // Paging 3 Integration
    implementation("androidx.room:room-paging:$room_version")

    // Paging3
    implementation("androidx.paging:paging-runtime-ktx:$paging_version")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    // Gson Converter (For Gson JSON parsing)
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")
    // OkHttp (Retrofit's recommended HTTP client)
    implementation("com.squareup.okhttp3:okhttp:$okhttp_version")
    // OkHttp Logging Interceptor (For logging network requests and responses)
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttp_version")
}

kapt {
    correctErrorTypes = true
}