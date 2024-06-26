plugins {

    id("com.android.application")
    id("com.google.gms.google-services")
    //alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.ocenus"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ocenus"
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
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    //noinspection UseTomlInstead
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
    //noinspection UseTomlInstead
    implementation("com.google.firebase:firebase-analytics")
    //noinspection UseTomlInstead
    implementation("com.google.firebase:firebase-auth")

    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")
    // Firebase - Storage
    implementation ("com.google.firebase:firebase-storage:20.3.0")
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("androidx.recyclerview:recyclerview:1.1.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.11.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.11.0")

    // https://mvnrepository.com/artifact/org.mindrot.bcrypt/bcrypt
    implementation("at.favre.lib:bcrypt:0.2.0")

    implementation ("androidx.appcompat:appcompat:1.2.0")

    implementation ("com.google.android.material:material:1.2.1")
    implementation ("com.jakewharton.threetenabp:threetenabp:1.3.1")


    implementation ("androidx.recyclerview:recyclerview:1.2.1")

    implementation ("androidx.cardview:cardview:1.0.0")


}