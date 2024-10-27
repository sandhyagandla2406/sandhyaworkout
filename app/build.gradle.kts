plugins {
    id("com.android.application")
    id("com.google.gms.google-services") // Apply the Google services plugin
}

android {
    namespace = "com.canadore.sandhyaworkout"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.canadore.sandhyaworkout"
        minSdk = 24
        targetSdk = 34
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
buildFeatures {
    viewBinding = true
}
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    dependencies {
        implementation(platform("com.google.firebase:firebase-bom:32.3.0")) // Use the latest BOM version
        implementation("com.google.firebase:firebase-auth") // No version number needed
        implementation("com.google.firebase:firebase-database")
    }

    implementation(libs.firebase.auth)

    // Additional dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
apply(plugin = "com.google.gms.google-services")
