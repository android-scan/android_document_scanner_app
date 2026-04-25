# Шпора: JSX → Compose без ошибок

Читай этот файл ПЕРЕД любой работой над экранами.
JSX-референсы лежат рядом: `design/screens/*.jsx`

---

## 1. Цвета — точные HEX

```kotlin
BgPrimary    = Color(0xFFF7F4EF)  // тёплый off-white, фон всех экранов
BgSecondary  = Color(0xFFEFEBE3)  // карточки, инпуты
BgTertiary   = Color(0xFFE8E2D6)  // разделители, неактивные точки
Surface      = Color(0xFFFDFBF8)  // модалки, диалоги
SurfaceRaised= Color(0xFFFFFFFF)  // карточки документов
TextPrimary  = Color(0xFF1C1917)  // основной текст
TextSecondary= Color(0xFF6B6560)  // вторичный текст
TextTertiary = Color(0xFFA09890)  // плейсхолдеры, мета
Accent       = Color(0xFF8B7355)  // aged gold — главный акцент
AccentLight  = Color(0xFFC4A882)  // светлое золото
AccentSubtle = Color(0xFFF0E8DC)  // фон за акцентными элементами
Danger       = Color(0xFF8B3A3A)  // удаление
Success      = Color(0xFF4A7C59)  // успех
```

---

## 2. Шрифты

Все шрифты **bundled** — лежат в `app/src/main/res/font/`.
Не используй GoogleFont провайдер — работает только с интернетом.

| Переменная | Файлы | Использование |
|---|---|---|
| `FontDisplay` | `cormorant_garamond_*.ttf` | Заголовки, названия экранов |
| `FontBody` | `dm_sans_*.ttf` | Весь UI-текст, кнопки |
| `FontMono` | `dm_mono_*.ttf` | Даты, метаданные, счётчики |

**Веса которые есть:**
- FontDisplay: Normal, Medium, SemiBold, Bold + Italic, SemiBoldItalic
- FontBody: Light(300), Normal, Medium, SemiBold
- FontMono: Normal, Medium

**Типичные сочетания из JSX:**
```
Заголовок экрана  → FontDisplay SemiBold 30–38sp
Подзаголовок      → FontBody Light 15–16sp lineHeight×1.5
Кнопка            → FontBody Medium 15sp letterSpacing 0.02em
Мета / дата       → FontMono Normal 10–12sp letterSpacing 0.1–0.15em UPPERCASE
Счётчик шага      → FontMono Normal 11sp letterSpacing 0.15em UPPERCASE
```

---

## 3. Обязательные элементы каждого экрана

### PaperGrain — тёплая текстура бумаги
Добавлять на ВСЕ светлые экраны. Без неё экран выглядит пластиково.

```kotlin
// Обязательно: graphicsLayer + CompositingStrategy.Offscreen на родителе!
Box(
    modifier = Modifier
        .fillMaxSize()
        .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
        .background(BgPrimary)
) {
    // ... содержимое экрана ...
    PaperGrain(modifier = Modifier.fillMaxSize())  // последним слоем
}
```

`PaperGrain()` живёт в `presentation/components/CommonComponents.kt`.

---

## 4. Как читать JSX и переводить в Compose

### Цвета
```
var(--accent)        → Accent
var(--accent-light)  → AccentLight
var(--accent-subtle) → AccentSubtle
var(--bg-primary)    → BgPrimary
var(--surface)       → Surface
var(--text-primary)  → TextPrimary
var(--text-secondary)→ TextSecondary
var(--text-tertiary) → TextTertiary
var(--bg-tertiary)   → BgTertiary
```

### Размеры
```
border-radius: var(--radius-sm) = 6dp  → RoundedCornerShape(6.dp)
border-radius: var(--radius-md) = 12dp → RoundedCornerShape(12.dp)
border-radius: var(--radius-lg) = 20dp → RoundedCornerShape(20.dp)
border-radius: var(--radius-xl) = 28dp → RoundedCornerShape(28.dp)
```

### Тени
```
box-shadow: var(--shadow-sm) → Modifier.shadow(elevation=2.dp, ambientColor=Accent.copy(0.06f))
box-shadow: var(--shadow-md) → Modifier.shadow(elevation=4.dp, ...)
box-shadow: var(--shadow-lg) → Modifier.shadow(elevation=12.dp, ...)
```

### Иллюстрации и иконки в JSX
**НИКОГДА не заменяй SVG-иллюстрации на Material Icons** — это главная причина
почему экраны выглядят хуже прототипа.

- Простые иконки в UI (кнопки, таббар) → Material Icons подходят
- Иллюстрации на слайдах, пустых состояниях → **Canvas с ручной отрисовкой SVG**

### Перевод SVG в Canvas
```kotlin
// SVG viewBox="0 0 200 180" → Canvas size 200dp × 180dp
Canvas(modifier = Modifier.size(width = 200.dp, height = 180.dp)) {
    val vw = 200f; val vh = 180f
    fun sx(x: Float) = x / vw * size.width   // пропорциональный перевод X
    fun sy(y: Float) = y / vh * size.height   // пропорциональный перевод Y
    // Дальше рисуем как в SVG
}
```

Соответствие SVG → Canvas:
```
<rect x y width height rx>  → drawRoundRect(topLeft, size, cornerRadius)
<path d="M...L...Z">        → Path().apply { moveTo; lineTo; close() } + drawPath
<circle cx cy r>            → drawCircle(center, radius)
<line x1 y1 x2 y2>         → drawLine(start, end)
stroke="var(--accent)"      → color = Accent, style = Stroke(width = X.dp.toPx())
fill="var(--bg-primary)"    → color = BgPrimary (без style = Stroke)
opacity="0.7"               → color = Accent.copy(alpha = 0.7f)
transform="rotate(-4 cx cy)"→ rotate(degrees = -4f, pivot = Offset(sx(cx), sy(cy))) { ... }
strokeLinecap="round"       → cap = StrokeCap.Round
```

---

## 5. Готовые компоненты

Все в `presentation/components/CommonComponents.kt`:

| Composable | Когда использовать |
|---|---|
| `PrimaryButton(text, onClick)` | Главная кнопка действия |
| `SecondaryButton(text, onClick)` | Второстепенное действие |
| `DocScannerTopBar(title, onBack)` | TopAppBar на всех экранах |
| `StatusChip(text, selected, onClick)` | PDF/JPG/PNG фильтры |
| `DocumentCard(name, date, pageCount, onClick)` | Карточка в списке |
| `PaperGrain()` | Текстура бумаги — на каждый экран |

---

## 6. Типичные паттерны из JSX

### Экран с прокруткой
```kotlin
Box(modifier = Modifier.fillMaxSize().background(BgPrimary)) {
    LazyColumn(modifier = Modifier.padding(bottom = 92.dp)) { // под TabBar
        // содержимое
    }
    PaperGrain()
}
```

### Диалог (iOS-стиль из JSX)
```kotlin
Dialog(onDismissRequest = { ... }) {
    Column(
        modifier = Modifier.clip(RoundedCornerShape(14.dp)).background(Surface)
    ) {
        // заголовок + текст с padding 24/20
        HorizontalDivider(color = BgTertiary)
        Row {
            // "Отмена" — TextSecondary | вертикальный разделитель | "Подтвердить" — Accent SemiBold
        }
    }
}
```

### Счётчик шага (PermissionsScreen, OnboardingScreen)
```kotlin
Text(
    text = "ШАГ ${step + 1} ИЗ $total",
    style = TextStyle(
        fontFamily = FontMono, fontWeight = FontWeight.Normal,
        fontSize = 11.sp, letterSpacing = 0.15.em, color = TextTertiary
    )
)
```

### Иконка в AccentSubtle-боксе
```kotlin
Box(
    modifier = Modifier.size(88.dp).clip(RoundedCornerShape(22.dp)).background(AccentSubtle),
    contentAlignment = Alignment.Center
) {
    Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(38.dp), tint = Accent)
}
```

---

## 7. Анимации

Из JSX: `animation: 'fadeIn 400ms ease-out both'`
```kotlin
// Простой fadeIn через alpha
var visible by remember { mutableStateOf(false) }
LaunchedEffect(Unit) { visible = true }
val alpha by animateFloatAsState(
    targetValue = if (visible) 1f else 0f,
    animationSpec = tween(durationMillis = 400),
    label = "fade"
)
// Modifier.alpha(alpha)
```

Стагированные (splash, как в JSX `delay: 200ms, 400ms`):
```kotlin
animationSpec = tween(durationMillis = 900, delayMillis = 200)
```

---

## 8. Что проверить перед коммитом

- [ ] На каждом светлом экране есть `PaperGrain()` последним слоем
- [ ] Родитель `PaperGrain` имеет `graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }`
- [ ] Иллюстрации — Canvas, не Material Icons
- [ ] Шрифты — через `R.font.*`, не `GoogleFont`
- [ ] `FontWeight.Light` для описательного текста (не Normal)
- [ ] Кнопка "Далее"/"Начать" — всегда видима, текст меняется (не прячется по alpha)
