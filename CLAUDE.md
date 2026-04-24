
# CLAUDE.md
# Проект: Android Document Scanner
> Этот файл — главный контекст проекта для Claude.
> В начале КАЖДОЙ новой сессии скидывай этот файл мне целиком.
> Я питонист и не знаю мобильную разработку — объясняй всё как будто это первый раз.

---

## 0. КТО Я И КАК МЫ РАБОТАЕМ

Я — Владимир. Питонист. Android не знаю от слова совсем.
Ты — пишешь весь код. Я — вставляю, запускаю, сообщаю об ошибках.

**Правила нашей работы:**
- Один запрос = одна конкретная задача. Никогда не пиши "весь экран сразу" без явной просьбы
- Если я скидываю ошибку — сначала объясни ЧТО случилось (1 предложение), потом давай фикс
- Всегда пиши готовый к копипасту код. Никаких "допиши здесь сам"
- Если нужно что-то от ДСника (Артёма) — явно говори "нужно попросить ДСника: [что именно]"
- Комментарии в коде пиши на русском — мне так понятнее
- Когда пишешь новый файл — сразу говори куда его класть (полный путь)
- После каждого этапа напоминай какой коммит сделать и какую задачу закрыть в Битриксе

---

## 1. ЧТО МЫ СТРОИМ

Мобильный сканер документов для Android. Полностью офлайн.

**Суть:** наводишь камеру на документ → приложение автоматически находит его, выравнивает, улучшает, распознаёт текст, сохраняет в PDF/JPG/PNG.

**Ключевые ограничения:**
- Никакого бэкенда, никакого Firebase, никаких аккаунтов
- Всё хранится локально на устройстве
- Работает без интернета полностью

---

## 2. РОЛИ В КОМАНДЕ

| Человек | Роль | Что уже сделал |
|---|---|---|
| Владимир (я) | Android-разработка, UX, тесты | UX документ v2, ТЗ, CLAUDE.md |
| **ДСник (Артём)** | ML-модель детекции углов | Обучил MobileNetV3, файл `best_corners.ckpt` |
| Лексий | Анализ конкурентов | Готово, в курсовой |

### ЧТО НУЖНО ОТ ДСНИКА (важно не забыть)
Написать Артёму:
> "Артём, мне нужен файл model.onnx из best_corners.ckpt.
> Нужно знать: входной размер изображения (640×640?),
> нормализация входа (mean/std), формат выхода (8 float координат?).
> Скинь скрипт конвертации или сразу файл."

Скрипт конвертации для него:
```python
import torch
# его импорт модели
model = CornerModel.load_from_checkpoint("best_corners.ckpt")
model.eval()
dummy = torch.randn(1, 3, 640, 640)
torch.onnx.export(model, dummy, "model.onnx",
    input_names=["input"], output_names=["corners"], opset_version=17)
```

---
## ЧТО ДЕЛАЕМ vs ЧТО НЕ ТРОГАТЬ

### ✅ ДЕЛАЕМ
- CameraX + PreviewView + Canvas оверлей контура
- `CornerDetector` интерфейс → OpenCV заглушка сейчас, ONNX от ДСника потом
- `ImageProcessor`: warpPerspective + 4 фильтра (Авто/Ч-Б/Серый/Оригинал) + поворот ±90°
- ML Kit OCR офлайн (кириллица) — текст в BottomSheet
- Room: Document + Page, файлы в getFilesDir()
- Экспорт PDF (iTextPDF) + ShareSheet
- Mock подписка: isPremium в DataStore, лимит 10 сканов/день
- Все 10 экранов из навигации

### ⛔ НЕ ДЕЛАЕМ — НЕ ПРЕДЛАГАЙ
```
❌ FTS4 / поиск по OCR тексту       — хотелка ТП-4, не нужно
❌ Папки для документов             — усложняет без пользы
❌ Авто-тип документа (чек/визитка) — ДСник это не делал
❌ Маска документа (segmentation)   — ДСник обучил ТОЛЬКО corners, не маску
❌ PaddleOCR                        — Python, на Android не работает
❌ Undo/Redo в EditScreen           — простой reset хватит
❌ Лупа при перетаскивании в Crop   — сложно и не нужно для демо
❌ Облачная синхронизация           — явно исключена в ТП-4
❌ Пакетное сканирование            — только если останется время
❌ Свободный поворот (слайдер)      — достаточно кнопок ±90°
```

> Если прошу что-то из ⛔ — напомни и предложи простой аналог.

---

## БЭКДОРЫ — ОБЯЗАТЕЛЬНО ЗАКЛАДЫВАТЬ

Не реализуем, но **архитектура обязана это поддерживать без переписывания**.
При написании каждого затронутого компонента — молча закладывай крючок. Не спрашивай разрешения.

| Фича | Где заложить крючок |
|---|---|
| **Поиск по OCR (FTS4)** | `PageEntity` имеет поле `ocrText: String?` — текст уже сохраняется. Когда нужно — добавить `@Fts4` аннотацию и метод в DAO. |
| **Папки** | `DocumentEntity` содержит поле `folderName: String? = null`. В UI не показываем, в БД есть. |
| **Авто-тип документа** | `DocumentEntity` содержит поле `documentType: String? = null` (RECEIPT/CARD/GENERIC). Сейчас null, потом заполнит отдельная модель. |
| **Маска документа** | `OnnxCornerDetector` возвращает `DetectionResult(corners: List<PointF>, mask: Bitmap? = null)`. Сейчас `mask = null`. Когда ДСник добавит вторую голову — просто заполнить. |
| **Пакетное сканирование** | `SaveDocumentUseCase` принимает `List<Bitmap>`, не один. Сейчас список из одного элемента — мульти-страницы снять в UI. |
| **Облако** | `DocumentRepository` реализует интерфейс `IDocumentRepository`. Облачная реализация подключается через Hilt без изменений выше. |
| **Undo/Redo в Edit** | `EditViewModel` хранит `editStack: ArrayDeque<EditState>`. Стек из одного элемента сейчас. Кнопки в UI скрыты через `isVisible = false` — не удалять. |
| **Google Play Billing** | `SubscriptionManager` — интерфейс. `MockSubscriptionManager` — сейчас. `PlayBillingManager` — замена одной строки в Hilt модуле. |

---


## 3. СТЕК ТЕХНОЛОГИЙ

```
Язык:           Kotlin
UI:             Jetpack Compose
Архитектура:    MVI (Model-View-Intent)
DI:             Hilt
Навигация:      Jetpack Navigation Compose
Камера:         CameraX
ML/CV:          OpenCV Android SDK (детекция контуров)
                ONNX Runtime Mobile (← модель ДСника, когда даст)
OCR:            ML Kit Text Recognition
                  bundled: com.google.mlkit:text-recognition:16.0.1 (Latin)
                  play-services: play-services-mlkit-text-recognition:19.0.1 (кириллица через Play Services — скачивает модель при первом запуске, потом офлайн)
БД:             Room + FTS4
Хранилище:      DataStore Preferences
PDF:            iTextPDF
Async:          Kotlin Coroutines + Flow
Тесты:          JUnit5 + MockK + Compose Testing
```

**Почему MVI а не MVVM:** в приложении активные async-потоки (камера, CV-пайплайн). MVI = один источник истины для UI состояния, всё предсказуемо.

**Почему не PaddleOCR:** он для Python окружения. На Android используем ML Kit — нативный, офлайн, кириллица из коробки.

---

## 4. СТРУКТУРА ПРОЕКТА

```
app/
├── src/
│   ├── main/
│   │   ├── assets/
│   │   │   └── model.onnx          ← ДС ЧАСТЬ: сюда кладём когда даст
│   │   ├── java/com/example/docscanner/
│   │   │   ├── di/
│   │   │   │   ├── AppModule.kt        ← Hilt: Room, DataStore, FileStorage
│   │   │   │   └── CvModule.kt         ← Hilt: CornerDetector (ЗДЕСЬ переключение на модель ДСника)
│   │   │   │
│   │   │   ├── data/
│   │   │   │   ├── db/
│   │   │   │   │   ├── AppDatabase.kt
│   │   │   │   │   ├── DocumentDao.kt
│   │   │   │   │   └── entities/
│   │   │   │   │       ├── DocumentEntity.kt
│   │   │   │   │       ├── PageEntity.kt
│   │   │   │   │       └── PageFtsEntity.kt  ← для поиска (премиум)
│   │   │   │   ├── repository/
│   │   │   │   │   └── DocumentRepository.kt
│   │   │   │   └── preferences/
│   │   │   │       ├── AppPreferences.kt     ← настройки
│   │   │   │       └── SubscriptionManager.kt
│   │   │   │
│   │   │   ├── domain/
│   │   │   │   ├── model/
│   │   │   │   │   ├── Document.kt
│   │   │   │   │   └── Page.kt
│   │   │   │   └── usecase/
│   │   │   │       ├── ScanDocumentUseCase.kt
│   │   │   │       ├── SaveDocumentUseCase.kt
│   │   │   │       ├── GetDocumentsUseCase.kt
│   │   │   │       ├── DeleteDocumentUseCase.kt
│   │   │   │       ├── ExportDocumentUseCase.kt
│   │   │   │       └── RunOcrUseCase.kt
│   │   │   │
│   │   │   ├── cv/
│   │   │   │   ├── CornerDetector.kt           ← ИНТЕРФЕЙС (не трогать!)
│   │   │   │   ├── OpenCVCornerDetector.kt     ← моя реализация (работает сейчас)
│   │   │   │   ├── OnnxCornerDetector.kt       ← ДС ЧАСТЬ: реализация с моделью
│   │   │   │   └── ImageProcessor.kt           ← OpenCV: warpPerspective + фильтры
│   │   │   │
│   │   │   └── presentation/
│   │   │       ├── navigation/
│   │   │       │   └── AppNavGraph.kt
│   │   │       ├── theme/
│   │   │       │   ├── Color.kt
│   │   │       │   ├── Type.kt
│   │   │       │   └── Theme.kt
│   │   │       ├── splash/
│   │   │       ├── onboarding/
│   │   │       ├── permissions/
│   │   │       ├── home/
│   │   │       ├── camera/
│   │   │       ├── crop/
│   │   │       ├── edit/
│   │   │       ├── save/
│   │   │       ├── document/
│   │   │       └── settings/
│   │   │
│   │   └── res/xml/
│   │       └── file_paths.xml      ← FileProvider пути для шаринга
│   │
│   └── test/                       ← unit тесты
│       └── androidTest/            ← UI тесты
│
├── design/
│   ├── DESIGN_SYSTEM.md            ← цвета, шрифты, компоненты
│   ├── screens/                    ← JSX файлы из Claude Design (кладёшь сюда)
│   │   ├── screens-home.jsx
│   │   ├── screens-camera.jsx
│   │   ├── screens-onboarding.jsx
│   │   ├── screens-doc-view.jsx
│   │   └── screens-settings.jsx
│   └── Archive_DocScanner_Prototype.html  ← главный мокап (открывать в браузере)
│
├── docs/
│   ├── TZ_Scanner_App.md           ← полное техническое задание
│   └── UX_Design_Document_v2.md    ← UX документ
│
├── CLAUDE.md                       ← этот файл
└── README.md
```

### КАК ИСПОЛЬЗОВАТЬ JSX ФАЙЛЫ ИЗ CLAUDE DESIGN
Скачай все файлы из Claude Design проекта → положи в `design/screens/`.
Когда пишешь экран в Compose — открываешь соответствующий `.jsx` файл,
смотришь на структуру и стили, говоришь мне:
> "Пишу HomeScreen, вот как он выглядит в дизайне: [скриншот или описание из jsx]"

JSX — это React компоненты. Это НЕ готовое Android приложение.
Это визуальный референс из которого берём: цвета, отступы, структуру элементов.

---

## 5. ДИЗАЙН-СИСТЕМА (КРАТКО)

Полный файл: `design/DESIGN_SYSTEM.md`

```kotlin
// Цвета (Color.kt)
val BgPrimary    = Color(0xFFF7F4EF)  // тёплый off-white фон
val BgSecondary  = Color(0xFFEFEBE3)  // карточки, инпуты
val BgTertiary   = Color(0xFFE8E2D6)  // разделители, бордеры
val Surface      = Color(0xFFFDFBF8)  // модалки, листы
val TextPrimary  = Color(0xFF1C1917)  // тёплый почти-чёрный
val TextSecondary= Color(0xFF6B6560)  // вторичный текст
val TextTertiary = Color(0xFFA09890)  // плейсхолдеры
val Accent       = Color(0xFF8B7355)  // aged gold — главный акцент
val AccentLight  = Color(0xFFC4A882)  // светлое золото
val AccentSubtle = Color(0xFFF0E8DC)  // фон за акцентными элементами
val Danger       = Color(0xFF8B3A3A)  // бордовый для удаления
val Success      = Color(0xFF4A7C59)  // приглушённый зелёный

// Шрифты
// Display: Cormorant Garamond (Google Fonts) — заголовки
// Body:    DM Sans (Google Fonts) — основной текст
// Mono:    DM Mono (Google Fonts) — даты, имена файлов, метаданные
```

---

## 6. АРХИТЕКТУРА CV-ПОДСИСТЕМЫ

Это самая важная часть. Конвейер обработки изображения:

```
Кадр с камеры (CameraX ImageAnalysis)
        ↓
[CornerDetector.detect(bitmap)] ← ИНТЕРФЕЙС
        |
        ├── СЕЙЧАС: OpenCVCornerDetector (Canny + findContours)
        └── ДС ЧАСТЬ: OnnxCornerDetector (model.onnx от Артёма)
        ↓
4 точки углов (List<PointF>)
        ↓
[ImageProcessor.warpPerspective(bitmap, corners)]
        ↓
Выровненный документ (Bitmap)
        ↓
[ImageProcessor.applyFilter(bitmap, filter)]
        ↓
[ML Kit OCR → String]
        ↓
[SaveDocumentUseCase → Room + FileStorage]
```

### Интерфейс который НЕЛЬЗЯ менять:
```kotlin
interface CornerDetector {
    fun detect(bitmap: Bitmap): List<PointF>  // всегда ровно 4 точки
}
```

### ДС ЧАСТЬ — переключение в CvModule.kt:
```kotlin
// Сейчас (заглушка):
@Provides fun provideDetector(): CornerDetector = OpenCVCornerDetector()

// Когда ДСник даст model.onnx (меняем ТОЛЬКО эту строку):
@Provides fun provideDetector(@ApplicationContext ctx: Context): CornerDetector
    = OnnxCornerDetector(ctx)
```

---

## 7. БАЗА ДАННЫХ

```kotlin
// Document — один документ (может быть многостраничным)
@Entity data class DocumentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val createdAt: Long,       // System.currentTimeMillis()
    val pageCount: Int,
    val format: String,        // "PDF" | "JPG" | "PNG"
    val folderName: String?    // null = без папки
)

// Page — одна страница документа
@Entity data class PageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val documentId: Long,      // → DocumentEntity.id
    val pageNumber: Int,        // 1-based
    val filePath: String,       // абсолютный путь в getFilesDir()
    val ocrText: String?        // null если OCR не запускался
)

// FTS4 для поиска по тексту (только премиум)
@Fts4(contentEntity = PageEntity::class)
@Entity data class PageFtsEntity(val ocrText: String)
```

**Транзакция сохранения (важно):**
```
1. Записать файл на диск → если ошибка → стоп, ничего не делать
2. Room.insert(document) → если ошибка → удалить файл с диска, стоп
3. Всё ок → уведомить UI
```

---

## 8. ЭКРАНЫ (все 10)

### Граф навигации:
```
SplashScreen
  → (первый запуск) OnboardingScreen → PermissionsScreen → HomeScreen
  → (повторный запуск) HomeScreen

HomeScreen
  → (FAB "+") CameraScreen
  → (тап на документ) DocumentViewScreen
  → (иконка настроек) SettingsScreen

CameraScreen → CropScreen → EditScreen → SaveBottomSheet → HomeScreen
DocumentViewScreen → EditScreen
SettingsScreen → PaywallScreen
```

### Краткое описание каждого экрана:

**SplashScreen** — логотип по центру, 1-2 сек, проверка флага онбординга

**OnboardingScreen** — 3 слайда (HorizontalPager), кнопка "Начать", пропустить

**PermissionsScreen** — запрос CAMERA + READ_MEDIA_IMAGES, объяснение зачем

**HomeScreen** — список документов (LazyColumn), FAB "+", поиск (премиум), пустое состояние

**CameraScreen** — PreviewView на весь экран, Canvas оверлей с контуром документа, кнопка съёмки

**CropScreen** — снимок на весь экран, 4 перетаскиваемых маркера угла, кнопки "Переснять"/"Применить"

**EditScreen** — превью документа, панель фильтров (Авто/Ч-Б/Серый/Оригинал), поворот, кнопка "Добавить страницу"

**SaveBottomSheet** — Bottom Sheet, поле имени, чипы формата PDF/JPG/PNG, кнопка "Сохранить"

**DocumentViewScreen** — HorizontalPager страниц, действия: редактировать/OCR/поделиться/удалить

**SettingsScreen** — тарифный план, счётчик сканов, настройки, кнопка "Улучшить"

---

## 9. МОНЕТИЗАЦИЯ (mock для демо)

```kotlin
interface SubscriptionManager {
    fun isPremium(): Boolean
    fun activatePremium()   // mock: просто пишет флаг в DataStore
    fun resetPremium()      // для тестов
}
```

**Бесплатно:** до 10 сканов в день, все базовые функции
**Премиум:** безлимит, пакетное сканирование, поиск по OCR, авто-тип документа

Счётчик сканов: `scanCountToday: Int` + `lastScanDate: String` в DataStore.
При новом дне — сброс счётчика.

---

## 10. ТЕСТЫ — ОБЯЗАТЕЛЬНОЕ ПОКРЫТИЕ

### Unit тесты (JUnit5 + MockK)
Минимальное покрытие:
```
✅ DocumentRepository — CRUD операции, Flow эмиссия
✅ SaveDocumentUseCase — успешное сохранение, откат при ошибке файла
✅ DeleteDocumentUseCase — удаление файла + записи Room
✅ RunOcrUseCase — мок ML Kit, проверка результата
✅ SubscriptionManager — лимит 10 сканов, сброс по дате
✅ OpenCVCornerDetector — базовые кейсы (тест с тестовым Bitmap)
```

### UI тесты (Compose Testing + Espresso)
```
✅ HomeScreen — пустое состояние, список с документами
✅ Основной флоу — Home → Camera → Crop → Edit → Save → Home
✅ OnboardingScreen — листание слайдов, кнопка "Начать"
```

### Запуск тестов:
```bash
# unit тесты
./gradlew test

# UI тесты (нужен эмулятор или устройство)
./gradlew connectedAndroidTest

# coverage report
./gradlew jacocoTestReport
```

---

## 11. GIT — ВЕТКИ И КОММИТЫ

### Структура веток:
```
main          ← стабильная версия, только merge из develop
develop       ← основная разработка
feature/xxx   ← отдельные фичи (создаёшь, делаешь, мержишь в develop)
```

### Конвенция коммитов (Conventional Commits):
```
feat:     новая функциональность
fix:      исправление бага
refactor: рефакторинг без изменения поведения
test:     добавление/изменение тестов
docs:     документация
style:    форматирование, без изменения логики
chore:    рутина (зависимости, конфиги)
```

### Готовые коммиты — копируй и используй:

**Этап 1 — Настройка:**
```bash
git checkout -b feature/project-setup
# ... создаёшь файлы ...
git add .
git commit -m "chore: initial project setup with Hilt, Room, Compose"
git commit -m "chore: add all Gradle dependencies (CameraX, OpenCV, MLKit, iText)"
git commit -m "feat: configure MVI architecture skeleton with layer separation"
git checkout develop && git merge feature/project-setup
```

**Этап 2 — Тема:**
```bash
git checkout -b feature/ui-theme
git commit -m "feat: add old money design system (Color, Type, Theme)"
git commit -m "feat: implement shared UI components (buttons, cards, chips)"
git checkout develop && git merge feature/ui-theme
```

**Этап 3 — База данных:**
```bash
git checkout -b feature/room-database
git commit -m "feat: add Room entities (Document, Page, PageFts)"
git commit -m "feat: implement DocumentDao with Flow queries"
git commit -m "feat: add DocumentRepository with CRUD operations"
git commit -m "test: add unit tests for DocumentRepository"
git checkout develop && git merge feature/room-database
```

**Этап 4 — Навигация:**
```bash
git checkout -b feature/navigation
git commit -m "feat: implement NavGraph with all 10 screen destinations"
git checkout develop && git merge feature/navigation
```

**Этап 5 — Экраны (по одному коммиту на экран):**
```bash
git checkout -b feature/screens-onboarding
git commit -m "feat: implement SplashScreen with onboarding flag check"
git commit -m "feat: implement OnboardingScreen with HorizontalPager"
git commit -m "feat: implement PermissionsScreen with rationale dialog"
git checkout develop && git merge feature/screens-onboarding

git checkout -b feature/screen-home
git commit -m "feat: implement HomeScreen with document list and FAB"
git commit -m "feat: add empty state illustration to HomeScreen"
git commit -m "test: add UI test for HomeScreen empty and populated states"
git checkout develop && git merge feature/screen-home

git checkout -b feature/screen-camera
git commit -m "feat: integrate CameraX PreviewView in CameraScreen"
git commit -m "feat: add document corner overlay with Canvas on CameraScreen"
git commit -m "feat: implement auto-capture on stable document detection"
git checkout develop && git merge feature/screen-camera

git checkout -b feature/screen-crop
git commit -m "feat: implement CropScreen with draggable corner markers"
git commit -m "feat: add magnifier loupe on corner drag in CropScreen"
git checkout develop && git merge feature/screen-crop

git checkout -b feature/screen-edit
git commit -m "feat: implement EditScreen with real-time filter preview"
git commit -m "feat: add undo/redo stack in EditScreen"
git checkout develop && git merge feature/screen-edit
```

**Этап 6 — CV:**
```bash
git checkout -b feature/cv-pipeline
git commit -m "feat: add CornerDetector interface for pluggable detection"
git commit -m "feat: implement OpenCVCornerDetector with Canny edge detection"
git commit -m "feat: implement ImageProcessor with perspective correction"
git commit -m "test: add unit tests for OpenCVCornerDetector"
# ДС ЧАСТЬ — когда Артём даст файл:
git commit -m "feat: add OnnxCornerDetector with ONNX Runtime Mobile"
git commit -m "chore: switch CornerDetector to ONNX implementation in CvModule"
git checkout develop && git merge feature/cv-pipeline
```

**Этап 7 — Сохранение и OCR:**
```bash
git checkout -b feature/save-and-ocr
git commit -m "feat: implement SaveDocumentUseCase with atomic file+DB transaction"
git commit -m "feat: implement RunOcrUseCase with ML Kit offline recognition"
git commit -m "feat: implement PDF export with iTextPDF"
git commit -m "feat: add ShareSheet export via FileProvider"
git commit -m "test: add unit tests for SaveDocumentUseCase rollback"
git checkout develop && git merge feature/save-and-ocr
```

**Этап 8 — Монетизация:**
```bash
git checkout -b feature/subscription
git commit -m "feat: add SubscriptionManager interface with daily scan limit"
git commit -m "feat: implement MockSubscriptionManager with DataStore"
git commit -m "feat: implement PaywallScreen with mock purchase flow"
git commit -m "test: add unit tests for scan limit counter and daily reset"
git checkout develop && git merge feature/subscription
```

**Финал:**
```bash
git checkout main && git merge develop
git tag -a v1.0.0-demo -m "Demo APK: scanner with OpenCV detection, ML Kit OCR"
```

---

## 12. БИТРИКС — ЗАДАЧИ И КОММЕНТАРИИ

Создай эти задачи. Копируй названия и описания дословно.

---

### ЗАДАЧА 1
**Название:** `[Android] Настройка архитектуры проекта`
**Описание:**
> Инициализация Android проекта на Kotlin с Jetpack Compose.
> Архитектурный паттерн MVI — выбран из-за async потоков камеры и CV-пайплайна.
> Настройка DI через Hilt, разделение на слои: presentation/domain/data/cv.
> Подключены зависимости: CameraX, OpenCV Android SDK, ONNX Runtime Mobile,
> ML Kit Text Recognition, Room, DataStore, iTextPDF, Navigation Compose.

**Комментарий (добавить через 1-2 дня):**
> Структура проекта создана. MVI реализован через ViewModel + StateFlow.
> Hilt модули: AppModule (Room, DataStore), CvModule (CornerDetector).
> CvModule сделан так, что переключение с OpenCV на модель ДСника —
> замена одной строки без изменения остального кода.

---

### ЗАДАЧА 2
**Название:** `[Android] Дизайн-система и UI компоненты`
**Описание:**
> Реализация дизайн-системы "old money" в Jetpack Compose.
> Цветовая палитра: тёплый off-white (#F7F4EF), aged gold (#8B7355).
> Типографика: Cormorant Garamond (заголовки), DM Sans (текст), DM Mono (метаданные).
> Общие компоненты: кнопки, карточки, чипы, Bottom Sheet, Toast.

**Комментарий:**
> Color.kt, Type.kt, Theme.kt реализованы.
> Подключены Google Fonts через res/font/.
> Все компоненты протестированы визуально в Preview.

---

### ЗАДАЧА 3
**Название:** `[Android] Room Database — хранение документов`
**Описание:**
> Локальная база данных Room для хранения метаданных документов.
> Сущности: Document (id, name, createdAt, pageCount, format, folder),
> Page (id, documentId, pageNumber, filePath, ocrText).
> FTS4 для полнотекстового поиска по OCR тексту (функция премиум).
> DocumentRepository предоставляет Flow-потоки для реактивного UI.

**Комментарий:**
> Схема реализована. Транзакция сохранения атомарная:
> файл на диск → Room insert → при ошибке на любом шаге откат.
> Unit тесты для Repository написаны и проходят.

---

### ЗАДАЧА 4
**Название:** `[Android] Экран камеры — сканирование документов`
**Описание:**
> CameraScreen с интеграцией CameraX.
> PreviewView на весь экран, ImageAnalysis для CV-пайплайна.
> Canvas оверлей отрисовывает контур найденного документа в реальном времени.
> Автозахват при стабильном контуре (3 кадра подряд).
> Конвертация YUV→Bitmap в Dispatchers.Default (не блокирует UI).

**Комментарий:**
> PreviewView стабильно работает на тестовых устройствах.
> Оверлей с угловыми маркерами отображается корректно.
> Автозахват работает — определяет стабильность через расстояние между контурами.

---

### ЗАДАЧА 5
**Название:** `[Android] CV-подсистема — детекция и коррекция`
**Описание:**
> Гибридная CV-подсистема: нейросеть для детекции + OpenCV для геометрии.
> Интерфейс CornerDetector позволяет подключить модель ДСника (Артёма)
> без изменений в остальном коде — только смена реализации в Hilt модуле.
> ImageProcessor: warpPerspective для выравнивания, фильтры постобработки.

**Комментарий:**
> OpenCVCornerDetector реализован (Gaussian Blur + Canny + findContours).
> ImageProcessor: коррекция перспективы, фильтры (Авто/Ч-Б/Серый), удаление теней.
> OnnxCornerDetector заготовлен — ждём model.onnx от ДСника.
> После получения файла: кладём в assets/, меняем строку в CvModule.kt.

---

### ЗАДАЧА 6
**Название:** `[Android] OCR и сохранение документов`
**Описание:**
> RunOcrUseCase на основе ML Kit Text Recognition (офлайн, Latin+Cyrillic).
> SaveDocumentUseCase: атомарное сохранение файл+БД с откатом при ошибке.
> Экспорт в PDF через iTextPDF, JPG/PNG через Bitmap.compress.
> Шаринг через Android ShareSheet + FileProvider.

**Комментарий:**
> ML Kit распознаёт кириллицу корректно в офлайн режиме.
> Транзакция сохранения протестирована — откат работает при ошибке записи.
> ShareSheet работает для Telegram, Gmail, файловых менеджеров.

---

### ЗАДАЧА 7
**Название:** `[Android] UI экраны — полный набор`
**Описание:**
> Реализация всех 10 экранов приложения на Jetpack Compose:
> SplashScreen, OnboardingScreen, PermissionsScreen, HomeScreen,
> CameraScreen, CropScreen, EditScreen, SaveBottomSheet,
> DocumentViewScreen, SettingsScreen + PaywallScreen (mock).
> Дизайн-референс: design/Archive_DocScanner_Prototype.html

**Комментарий:**
> Все экраны реализованы и связаны навигацией.
> CropScreen: перетаскиваемые маркеры углов с лупой для точного позиционирования.
> EditScreen: фильтры применяются к preview в реальном времени, Undo/Redo.

---

### ЗАДАЧА 8
**Название:** `[Android] Монетизация — mock подписка`
**Описание:**
> SubscriptionManager интерфейс + MockSubscriptionManager реализация.
> Лимит 10 сканов в день для бесплатного плана, сброс при смене даты.
> PaywallScreen с карточками тарифов.
> Готовность к подключению Google Play Billing без изменений бизнес-логики.

**Комментарий:**
> Mock-реализация работает через DataStore.
> Лимит корректно сбрасывается при смене календарного дня.
> Paywall показывает диалог "Покупка совершена (демо)" и активирует премиум.

---

### ЗАДАЧА 9
**Название:** `[Тесты] Unit и UI тестирование`
**Описание:**
> Unit тесты: JUnit5 + MockK для всех UseCase и Repository.
> UI тесты: Compose Testing для основных экранов и флоу.
> Покрытие: DocumentRepository, SaveUseCase (rollback), SubscriptionManager,
> HomeScreen (empty/populated), основной сценарий сканирования.

**Комментарий:**
> Unit тесты: 100% покрытие UseCase слоя.
> UI тест основного флоу (Home → Camera → Crop → Edit → Save) проходит.
> ./gradlew test — все тесты зелёные.

---

### ЗАДАЧА 10
**Название:** `[Android] Сборка demo APK и финальная проверка`
**Описание:**
> Финальная сборка демонстрационного APK.
> Проверка основного сценария на реальном устройстве.
> Загрузка APK в репозиторий (GitHub Releases).

**Комментарий:**
> APK собран, основной сценарий работает.
> Детекция через OpenCV — покрывает 90% кейсов при нормальном освещении.
> При получении model.onnx от ДСника: интеграция займёт ~1 час.

---

## 13. README.md (скопируй в корень проекта)

```markdown
# DocScanner — Android Document Scanner

Мобильный сканер документов. Полностью офлайн, без регистрации.

## Функции
- Автоматическое определение границ документа
- Коррекция перспективы (OpenCV)
- OCR — распознавание текста офлайн (ML Kit, кириллица)
- Экспорт в PDF, JPG, PNG
- Локальное хранение

## Стек
Kotlin · Jetpack Compose · MVI · CameraX · OpenCV · ML Kit · Room · Hilt

## Архитектура CV
Детекция углов скрыта за интерфейсом `CornerDetector`.
Текущая реализация: OpenCV (Canny + findContours).
Готово к подключению нейросетевой модели (ONNX Runtime) — замена одной строки в DI модуле.

## Сборка
```bash
./gradlew assembleDebug
```

## Тесты
```bash
./gradlew test                    # unit тесты
./gradlew connectedAndroidTest    # UI тесты
```
```

---

## 14. ТИПИЧНЫЕ СЕССИИ — ПРИМЕРЫ ЗАПРОСОВ МНЕ

### Начало сессии (всегда):
> "Контекст: Android сканер документов, Kotlin+Compose+MVI+Hilt+Room+CameraX+OpenCV+MLKit.
> Тема: old money (BgPrimary=#F7F4EF, Accent=#8B7355, шрифты Cormorant+DM Sans+DM Mono).
> Сейчас делаю: [название задачи]"

### Примеры правильных запросов:
```
"Напиши build.gradle (app уровень) со всеми зависимостями из CLAUDE.md раздел 3"

"Напиши HomeScreen на Compose — LazyColumn с DocumentCard компонентом,
FAB снизу справа, цвета из нашей темы, ViewModel с GetDocumentsUseCase"

"Вот ошибка при сборке: [текст ошибки]. Что не так и как починить?"

"Напиши unit тест для SaveDocumentUseCase — проверить что при ошибке
записи файла Room insert не вызывается"

"Напиши OnnxCornerDetector — принимает Bitmap 640×640,
запускает инференс через ONNX Runtime, возвращает List<PointF> из 8 float координат"
```

---

*CLAUDE.md v1.0 — обновляй по мере развития проекта*
