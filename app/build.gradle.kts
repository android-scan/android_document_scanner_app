plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.android_document_scanner_app"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.android_document_scanner_app"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    // Исключаем конфликтующие файлы лицензий из iTextPDF и других библиотек
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/NOTICE.md"
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/DEPENDENCIES"
        }
    }
}

dependencies {

    // ── Compose BOM — единая версия для всех Compose библиотек ──────────────
    val composeBom = platform("androidx.compose:compose-bom:2026.02.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Compose UI
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // ── Core Android ─────────────────────────────────────────────────────────
    implementation("androidx.core:core-ktx:1.18.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.10.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
    implementation("androidx.activity:activity-compose:1.13.0")

    // ── Navigation ───────────────────────────────────────────────────────────
    implementation("androidx.navigation:navigation-compose:2.9.8")

    // ── Hilt — Dependency Injection ──────────────────────────────────────────
    implementation("com.google.dagger:hilt-android:2.59.2")
    ksp("com.google.dagger:hilt-compiler:2.59.2")
    implementation("androidx.hilt:hilt-navigation-compose:1.3.0")

    // ── Room — локальная БД ──────────────────────────────────────────────────
    implementation("androidx.room:room-runtime:2.8.4")
    implementation("androidx.room:room-ktx:2.8.4")
    ksp("androidx.room:room-compiler:2.8.4")

    // ── DataStore — настройки и подписка ─────────────────────────────────────
    implementation("androidx.datastore:datastore-preferences:1.2.1")

    // ── CameraX — камера ─────────────────────────────────────────────────────
    implementation("androidx.camera:camera-core:1.6.0")
    implementation("androidx.camera:camera-camera2:1.6.0")
    implementation("androidx.camera:camera-lifecycle:1.6.0")
    implementation("androidx.camera:camera-view:1.6.0")

    // ── OpenCV — детекция контуров документа ─────────────────────────────────
    implementation("org.opencv:opencv:4.13.0")

    // ── ONNX Runtime — нейросеть ДСника (когда даст model.onnx) ─────────────
    implementation("com.microsoft.onnxruntime:onnxruntime-android:1.25.0")

    // ── ML Kit — офлайн OCR (Latin bundled, Cyrillic через Play Services) ───
    // Bundled модель (offline, Latin script — для кириллицы нужен Tesseract или Play Services)
    implementation("com.google.mlkit:text-recognition:16.0.1")
    // Play Services версия скачивает модели автоматически и поддерживает кириллицу
    implementation("com.google.android.gms:play-services-mlkit-text-recognition:19.0.1")

    // ── iTextPDF — экспорт в PDF ─────────────────────────────────────────────
    implementation("com.itextpdf:itextpdf:5.5.13.5")

    // ── Coroutines ───────────────────────────────────────────────────────────
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    // ── Unit тесты (JUnit5 + MockK) ──────────────────────────────────────────
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:6.0.3")
    testImplementation("io.mockk:mockk:1.14.9")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")

    // ── UI тесты (Compose Testing + Espresso) ────────────────────────────────
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}

// Устанавливает JVM 17 для Kotlin компилятора
kotlin {
    jvmToolchain(17)
}

// Включает JUnit 5/6 для unit тестов
tasks.withType<Test> {
    useJUnitPlatform()
}
