# Design System — Document Scanner App
## Aesthetic: Old Money · iPhone-native · Light theme

---

## DESIGN PHILOSOPHY

**Concept:** Refined restraint. Like a leather-bound Moleskine from a Geneva stationer — nothing loud, nothing cheap, everything intentional. The app feels like it costs more than it does.

**NOT:** Fintech brutalism. NOT Material Design. NOT startup blue. NOT skeuomorphic leather textures.

**YES:** Cream paper. Aged gold. Quiet confidence. Editorial whitespace. The kind of UI that makes users feel organised and intelligent.

---

## COLOR PALETTE

```
--color-bg-primary:      #F7F4EF   /* warm off-white, like aged paper */
--color-bg-secondary:    #EFEBE3   /* slightly warmer, card backgrounds */
--color-bg-tertiary:     #E8E2D6   /* borders, dividers */

--color-surface:         #FDFBF8   /* pure near-white for sheets, modals */
--color-surface-raised:  #FFFFFF   /* only for top-layer cards */

--color-text-primary:    #1C1917   /* warm near-black, NOT pure black */
--color-text-secondary:  #6B6560   /* muted warm grey */
--color-text-tertiary:   #A09890   /* placeholders, hints */

--color-accent:          #8B7355   /* aged gold / warm bronze — THE signature color */
--color-accent-light:    #C4A882   /* lighter gold for highlights */
--color-accent-subtle:   #F0E8DC   /* tinted backgrounds behind accent elements */

--color-success:         #4A7C59   /* muted sage green */
--color-danger:          #8B3A3A   /* muted burgundy */
--color-warning:         #8B6914   /* dark amber */

--color-overlay:         rgba(28, 25, 23, 0.6)  /* modal scrim */
--color-camera-accent:   #C4A882   /* corner markers on camera overlay */
```

---

## TYPOGRAPHY

```
Display font:   "Cormorant Garamond" — for headings, large titles
Body font:      "DM Sans" — clean, slightly geometric, pairs perfectly
Mono font:      "DM Mono" — for file names, dates, metadata

Scale:
--text-xs:    11sp
--text-sm:    13sp
--text-base:  15sp  ← default body
--text-md:    17sp  ← primary labels
--text-lg:    20sp  ← section headers
--text-xl:    24sp  ← screen titles
--text-2xl:   30sp  ← display / hero
--text-3xl:   38sp  ← onboarding hero

Weights:
Light (300)   → onboarding subtitles, hints
Regular (400) → body text, secondary labels
Medium (500)  → primary labels, buttons
SemiBold (600)→ screen titles
Bold (700)    → document names, key numbers

Letter spacing:
Headers:       +0.02em (slight tracking, feels editorial)
ALL CAPS:      +0.12em (labels, chips, tags)
Body:          0
```

---

## SPACING & SHAPE

```
Base unit: 8dp

--space-xs:   4dp
--space-sm:   8dp
--space-md:   16dp
--space-lg:   24dp
--space-xl:   32dp
--space-2xl:  48dp
--space-3xl:  64dp

Corner radii:
--radius-sm:  6dp   → chips, small tags
--radius-md:  12dp  → cards, sheets
--radius-lg:  20dp  → bottom sheets, modals
--radius-xl:  28dp  → FAB, large cards
--radius-full: 999dp → pills, avatar

Shadows (warm-tinted, not grey):
--shadow-sm:  0 1px 3px rgba(28,25,23,0.08), 0 1px 2px rgba(28,25,23,0.04)
--shadow-md:  0 4px 12px rgba(28,25,23,0.10), 0 2px 4px rgba(28,25,23,0.06)
--shadow-lg:  0 12px 32px rgba(28,25,23,0.14), 0 4px 8px rgba(28,25,23,0.08)
```

---

## COMPONENTS

### Navigation Bar (bottom)
- Background: `--color-bg-primary` with subtle top border `--color-bg-tertiary`
- 3 items max: Home, Scan (center, raised), Settings
- Active icon: `--color-accent` filled
- Inactive icon: `--color-text-tertiary` outlined
- Center Scan button: raised oval, `--color-accent` background, white icon, shadow-md

### Top App Bar
- Background: transparent over `--color-bg-primary`
- Title: Cormorant Garamond SemiBold, 22sp, `--color-text-primary`
- Icons: `--color-text-secondary`, 24dp
- No elevation, no shadow — flat and clean
- iOS-style: large title collapses on scroll

### Cards (Document list items)
- Background: `--color-surface-raised`
- Border: 1dp `--color-bg-tertiary`
- Corner: `--radius-md`
- Shadow: `--shadow-sm`
- Thumbnail: left side, 56×72dp, rounded corners `--radius-sm`
- Title: DM Sans Medium 15sp, `--color-text-primary`
- Meta (date, pages): DM Mono 12sp, `--color-text-tertiary`
- On press: scale 0.98, shadow disappears (feels physical)

### Primary Button
- Background: `--color-accent`
- Text: DM Sans Medium 15sp, #FFFFFF, letter-spacing +0.02em
- Padding: 14dp vertical, 24dp horizontal
- Corner: `--radius-md`
- No border
- Press state: darken 8%, scale 0.97

### Secondary Button
- Background: transparent
- Border: 1.5dp `--color-accent`
- Text: `--color-accent`
- Same sizing as primary

### Chip / Filter Tag
- Background: `--color-accent-subtle`
- Text: DM Sans Medium 12sp, `--color-accent`, ALL CAPS, +0.12em tracking
- Padding: 6dp vertical, 12dp horizontal
- Corner: `--radius-sm`
- Selected: Background `--color-accent`, text white

### Bottom Sheet
- Background: `--color-surface`
- Top handle: 4×40dp, `--color-bg-tertiary`, centered, 8dp from top
- Corner top: `--radius-lg`
- Shadow: `--shadow-lg`

### FAB (Scan button)
- Size: 56×56dp center, or 64×64dp if prominent
- Background: `--color-accent`
- Icon: white camera, 28dp
- Corner: `--radius-xl`
- Shadow: `--shadow-md`

### Text Input
- Background: `--color-bg-secondary`
- Border: 1dp `--color-bg-tertiary`; focused: 1.5dp `--color-accent`
- Corner: `--radius-md`
- Label: DM Sans Regular 13sp, `--color-text-secondary`, floats up on focus
- Text: DM Sans Regular 15sp, `--color-text-primary`

### Toast / Snackbar
- Background: `--color-text-primary` (dark warm)
- Text: #F7F4EF, DM Sans Regular 14sp
- Corner: `--radius-md`
- Appears from bottom, 16dp margin from nav bar

---

## CAMERA OVERLAY STYLE

- Document corner markers: thin `--color-camera-accent` lines, L-shaped, 24dp arms, 2dp stroke
- Searching state: dashed white outline, animated dash-offset
- Found state: solid `--color-camera-accent` outline, subtle glow
- Status text: DM Sans Medium 14sp, white with dark blur-scrim behind
- Shutter button: 72dp white circle, 4dp `--color-accent` ring, inner circle 52dp
- All controls on dark translucent bar at top and bottom (not floating)

---

## ICONOGRAPHY

- Style: Rounded line icons, 2dp stroke weight
- Size: 24dp standard, 20dp in dense contexts, 28dp in bottom nav
- Library reference: Lucide Icons (matches the weight and roundness)
- Never filled icons except: active nav items, action buttons

---

## ANIMATIONS

- Page transitions: iOS-style horizontal slide (shared element on document open)
- Bottom sheet: spring, damping 0.8, from bottom
- Card press: scale 0.98, 80ms ease-out
- Camera overlay appear: fade + slight scale from 0.95, 200ms
- Filter apply: cross-fade preview, 150ms
- Saving: progress bar `--color-accent`, thin 2dp at top of screen
- Onboarding slide: fade + translateY(16dp), staggered 100ms per element

---

## SCREEN-SPECIFIC NOTES

### Onboarding
- Full bleed background: `--color-bg-primary` with very subtle paper grain texture overlay (5% opacity noise)
- Illustration style: thin line drawings, `--color-accent` strokes on cream
- Progress dots: 6dp, `--color-accent` active, `--color-bg-tertiary` inactive
- "Skip" button: top right, DM Sans Regular 14sp, `--color-text-tertiary`

### Home Screen
- Greeting area: Cormorant Garamond Light 28sp "Your documents" — editorial feel
- List/grid toggle: top right, icon switch
- Empty state: large thin-line illustration, centered, `--color-text-tertiary` text
- Section headers: DM Sans Medium 11sp, ALL CAPS, +0.12em tracking, `--color-text-tertiary`

### Camera Screen
- Near-black overlay (#0F0D0B) around document frame
- Status bar text white
- Top bar: blur background, back button + flash toggle + gallery button
- Bottom bar: blur background, shutter button centered, page counter right

### Document View
- White background for document pages (to show the scanned content clearly)
- AppBar transparent, collapses into document
- Page number: DM Mono 12sp, `--color-text-tertiary`, centered

### Settings / Paywall
- Premium badge: Cormorant Garamond Italic, `--color-accent`, thin gold border
- Plan cards: side-by-side or stacked, accent border on selected
- Price: Cormorant Garamond Bold 32sp for the number, DM Sans for "/мес"

---

# PROMPTS FOR CLAUDE DESIGN

Paste these one by one into Claude Design after uploading this document as your design system.

---

## GLOBAL SYSTEM PROMPT (paste first, pin as context)

```
Design system: Old Money aesthetic for an Android document scanner app. 
Light theme. Warm off-white backgrounds (#F7F4EF). Aged gold accent (#8B7355). 
Typography: Cormorant Garamond for display, DM Sans for body, DM Mono for metadata.
Warm near-black text (#1C1917). Cards with warm shadows. iOS-native feel on Android.
Rounded corners (12dp cards, 20dp sheets). Refined, quiet, editorial.
No purple. No blue. No Material Design defaults. No generic gradients.
Every screen feels like it belongs to a luxury productivity app.
```

---

## PROMPT 1 — Splash Screen

```
Design a splash screen for a document scanner app. 
Style: old money, warm off-white background (#F7F4EF), centered app logo — 
a minimal line-art icon of a document with a subtle corner fold, 
in aged gold (#8B7355). Below it: app name in Cormorant Garamond SemiBold 28sp.
No tagline. No animation description needed — just the static screen.
iPhone 15 Pro dimensions. Status bar light content.
Feels like opening a premium leather notebook brand app.
```

---

## PROMPT 2 — Onboarding (3 slides)

```
Design 3 onboarding slides for a document scanner app. iPhone 15 Pro.
Style: old money editorial. Background: warm off-white (#F7F4EF) with 
barely visible paper grain texture.

Slide 1: Large thin-line illustration of a phone scanning a document, 
gold accent lines (#8B7355). Headline: "Сканируйте любые документы" 
in Cormorant Garamond Bold 34sp. Subtitle: DM Sans Light 16sp warm grey.

Slide 2: Illustration of text being recognized (words highlighted in gold).
Headline: "Автоматическое распознавание текста"

Slide 3: Illustration of organized document folders.
Headline: "Храните и делитесь удобно"
Large primary button "Начать" in aged gold (#8B7355) at bottom.
Progress dots: 3 dots, gold active. Skip button top right in muted grey.
Generous whitespace. Feels unhurried and premium.
```

---

## PROMPT 3 — Home Screen (document list)

```
Design a home screen for a document scanner app. iPhone 15 Pro.
Style: old money, light theme.

Top: Large title "Документы" in Cormorant Garamond SemiBold 30sp, 
warm near-black (#1C1917). Right: grid/list toggle icon + settings icon.

Below title: search bar, background #EFEBE3, subtle border, 
placeholder "Поиск по документам..." in DM Sans, muted.

Document list items (show 4-5):
- Warm white card (#FFFFFF), 1dp warm border, shadow
- Left: document thumbnail (56×72dp), slightly rounded
- Right: document name DM Sans Medium 15sp, date DM Mono 12sp muted, 
  page count chip in gold-tinted background
- Swipe left hint on one card showing burgundy delete

Bottom navigation bar: 3 items — Documents (active, gold icon), 
center raised oval FAB with camera icon (gold background), Settings.
Bottom nav: warm white, subtle top border.

Empty area below list shows subtle section label "НЕДАВНИЕ" 
in DM Sans 11sp ALL CAPS tracking wide, warm grey.
Overall: calm, organized, looks like a premium notes app.
```

---

## PROMPT 4 — Camera Screen

```
Design a camera viewfinder screen for a document scanner app. iPhone 15 Pro.
Style: old money meets precision instrument.

Full-bleed camera preview (dark, #0F0D0B surround outside document frame).
Center: document detection overlay — L-shaped corner markers in aged gold 
(#C4A882), thin 2dp strokes, 24dp arms. Inside the frame: the "document" 
area is slightly lighter. Thin gold rectangle connecting the corners.

Status text centered above frame: "Документ обнаружен — держите ровно" 
DM Sans Medium 14sp white, on dark blur pill background.

Top bar: frosted glass dark, back arrow left, flash icon right, gallery icon right.
Bottom bar: frosted glass dark. Center: large shutter button — white 72dp circle, 
4dp gold ring around it. Left: page counter "Стр. 1". Right: blank or tip.

Feels like a precision optical instrument, not a casual camera.
```

---

## PROMPT 5 — Crop Screen (corner adjustment)

```
Design a document corner adjustment screen. iPhone 15 Pro.
Style: old money, precision.

Full-bleed scanned document photo as background (show a paper document photo).
Four corner drag handles: circular markers 20dp, gold fill (#8B7355), 
white center dot 8dp. Thin gold lines connecting the 4 handles forming a quadrilateral.

At one corner (top-right being dragged): magnifier loupe circle 80dp, 
showing zoomed view of that corner for precision adjustment.

Bottom bar: white frosted, two buttons side by side:
Left: "Переснять" — secondary style, gold border, gold text.
Right: "Применить" — primary, gold fill, white text.

Top: just a back arrow, transparent bar.
Minimal UI — the document fills the screen, the handles are elegant.
```

---

## PROMPT 6 — Edit Screen (filters & adjustments)

```
Design a document editing screen. iPhone 15 Pro.
Style: old money editorial.

Top 60% of screen: document preview on slightly warm background (#F7F4EF),
subtle shadow, rounded corners. Shows a clean scanned document.

Top bar: "Редактирование" title Cormorant Garamond 20sp, 
undo arrow left, redo arrow, "Готово" button right in gold.

Bottom panel: warm white (#FDFBF8), top rounded corners.
Two rows:
Row 1 — Filter chips in horizontal scroll:
"Авто" (selected — gold fill, white text), "Ч/Б", "Серый", "Оригинал"
Chips: rounded 6dp, DM Sans Medium 12sp ALL CAPS.

Row 2 — Tool icons in horizontal row with labels:
Crop icon + "Обрезка", Rotate + "Поворот", Sparkles + "Улучшить", 
Wand + "Тени" — each in a tappable cell, gold when active.

Bottom: two buttons — "+ Добавить страницу" (secondary) and "Сохранить всё" (primary gold).
Feels like Darkroom or VSCO but for documents.
```

---

## PROMPT 7 — Save Bottom Sheet

```
Design a save document bottom sheet modal. iPhone 15 Pro.
Style: old money, refined.

Background screen blurred behind. Bottom sheet rises 60% of screen.
Top handle: 40×4dp warm grey pill, centered.
Title: "Сохранить документ" Cormorant Garamond SemiBold 22sp.

Content:
1. Text input field: "Название документа" label, 
   placeholder "Скан 24.04.2026 14:35" in DM Mono 14sp.
   Background #EFEBE3, gold bottom border when focused.

2. Format selection: three equally-sized chips in a row:
   "PDF" | "JPG" | "PNG" — gold-tinted background, 
   "PDF" selected (gold fill).

3. Optional folder row: folder icon + "Выбрать папку" + chevron right.
   DM Sans 15sp, warm grey.

4. Large primary button "Сохранить" full width, gold background.

Sheet background: #FDFBF8. Generous internal padding 24dp.
```

---

## PROMPT 8 — Document View Screen

```
Design a document viewer screen. iPhone 15 Pro.
Style: old money, clean reading experience.

Background: #F7F4EF warm off-white.

Top: AppBar transparent. Document title in Cormorant Garamond SemiBold 20sp.
Right: "..." menu icon.

Center: Large document page displayed as a card —
white background, warm shadow, rounded 12dp corners, slight page curl hint.
Shows a clean scanned document page with text.
Page counter below: "2 / 5" in DM Mono 13sp, centered, muted gold (#C4A882).
Horizontal swipe hint arrows barely visible on sides.

Bottom bar: warm white, subtle border top. Four action buttons with icons + labels:
[Редактировать] [OCR / Текст] [Поделиться] [Удалить]
First three: gold icon + DM Sans 12sp label.
Delete: muted burgundy icon.

OCR text preview bottom sheet (show partially open):
Rounded top corners, handle, title "Распознанный текст",
text content in DM Sans 14sp, "Копировать" button secondary style.
```

---

## PROMPT 9 — Settings / Subscription Screen

```
Design a settings and subscription screen. iPhone 15 Pro.
Style: old money premium.

Top: "Настройки" Cormorant Garamond SemiBold 28sp, large title style.

Subscription card (top, prominent):
Warm card (#FFFFFF), gold border 1.5dp, corner radius 16dp, shadow.
Left: badge "ПРЕМИУМ" in Cormorant Garamond Italic 14sp, all caps, gold color.
Center: "149 ₽ / месяц" — "149 ₽" in Cormorant Garamond Bold 32sp gold, 
"/ месяц" DM Sans Regular 16sp muted.
Right: "999 ₽ / год" secondary price, "Выгоднее на 30%" tag in sage green.
Below: "Улучшить до Премиум" full-width primary gold button.
If free plan: scan counter bar — thin progress bar in gold, 
"7 из 10 сканов сегодня" DM Sans 13sp below.

Settings list below (grouped):
Group 1: "Приложение" header ALL CAPS tracking.
- Язык: row with value "Русский" right
- Формат экспорта: "PDF"
- Качество: "Высокое"
Group 2:
- Очистить кэш: muted burgundy text

List items: warm white cards, warm dividers, DM Sans 15sp.
Chevrons on navigable rows.
```

---

## PROMPT 10 — Mock Paywall Screen

```
Design a premium upgrade paywall screen. iPhone 15 Pro.
Style: old money luxury, conversion-focused but never desperate.

Background: #F7F4EF warm.
Top: back button. Title: "Сканер Премиум" Cormorant Garamond SemiBold 28sp.
Subtitle: "Без ограничений. Без рекламы." DM Sans Light 16sp muted.

Feature list (4 items, generous spacing):
Each: gold checkmark icon + DM Sans Regular 15sp description
- "Безлимитное сканирование"
- "Пакетное сканирование нескольких страниц"  
- "Поиск по тексту документов"
- "Автоопределение типа документа"

Pricing cards (2, side by side or stacked):
Card 1 "Месяц": border 1dp warm grey, "149 ₽" Cormorant 28sp, "/мес" DM Sans.
Card 2 "Год" (recommended): gold border 1.5dp, gold "ВЫГОДНО" chip top-right, 
"999 ₽" Cormorant 28sp gold, "/год" DM Sans, "= 83 ₽/мес" small muted below.

Primary CTA button: full width, gold, "Попробовать Премиум".
Below: "Демо-версия: покупка не спишется" DM Mono 11sp, muted, centered.

Feels like a Monocle magazine subscription page, not an app store popup.
```

---

# HOW TO USE IN CLAUDE DESIGN

1. Upload THIS FILE (DESIGN_SYSTEM.md) to Claude Design as your brand asset
2. Paste the GLOBAL SYSTEM PROMPT first — pin it or keep it in your project context
3. Paste each numbered prompt one by one, generate, iterate
4. Save outputs to `/design/screens/` in your project
5. In CLAUDE.md reference: `Design system: see /design/DESIGN_SYSTEM.md`

---

*Design System v1.0 — Old Money · Light · Android Scanner App*
