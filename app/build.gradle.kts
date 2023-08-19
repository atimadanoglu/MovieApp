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
    val roomVersion = "2.5.2"
    val pagingVersion = "3.2.0"
    val hiltVersion = "2.44.2"
    val retrofitVersion = "2.9.0"
    val okhttpVersion = "4.10.0"
    val activityVersion = "1.7.2"
    val koinAndroidVersion = "3.4.3"

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Hilt
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")

    // Room
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    // To use Kotlin Symbol Processing (KSP)
    ksp("androidx.room:room-compiler:$roomVersion")
    // Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$roomVersion")
    // Paging 3 Integration
    implementation("androidx.room:room-paging:$roomVersion")

    // Paging3
    implementation("androidx.paging:paging-runtime-ktx:$pagingVersion")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    // Gson Converter (For Gson JSON parsing)
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    // OkHttp (Retrofit's recommended HTTP client)
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    // OkHttp Logging Interceptor (For logging network requests and responses)
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")


    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    // Lifecycles only (without ViewModel or LiveData)
    implementation( "androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

    // Kotlin
    implementation("androidx.activity:activity-ktx:$activityVersion")
    implementation("androidx.fragment:fragment-ktx:1.6.1")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")

    // Koin
    implementation ("io.insert-koin:koin-android:$koinAndroidVersion")
    // Koin Test features
    testImplementation ("io.insert-koin:koin-test:$koinAndroidVersion")
    // Koin for JUnit 4
    testImplementation ("io.insert-koin:koin-test-junit4:$koinAndroidVersion")
    // Navigation Graph
    implementation ("io.insert-koin:koin-androidx-navigation:$koinAndroidVersion")
}

kapt {
    correctErrorTypes = true
}