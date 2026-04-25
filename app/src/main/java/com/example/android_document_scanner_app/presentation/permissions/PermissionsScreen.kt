package com.example.android_document_scanner_app.presentation.permissions

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.android_document_scanner_app.presentation.components.PrimaryButton
import com.example.android_document_scanner_app.presentation.theme.Accent
import com.example.android_document_scanner_app.presentation.theme.AccentSubtle
import com.example.android_document_scanner_app.presentation.theme.BgPrimary
import com.example.android_document_scanner_app.presentation.theme.BgTertiary
import com.example.android_document_scanner_app.presentation.theme.FontBody
import com.example.android_document_scanner_app.presentation.theme.FontDisplay
import com.example.android_document_scanner_app.presentation.theme.FontMono
import com.example.android_document_scanner_app.presentation.theme.Surface
import com.example.android_document_scanner_app.presentation.theme.TextPrimary
import com.example.android_document_scanner_app.presentation.theme.TextSecondary
import com.example.android_document_scanner_app.presentation.theme.TextTertiary

private data class PermStep(
    val icon:       ImageVector,
    val title:      String,
    val shortTitle: String,
    val desc:       String,
)

private val STEPS = listOf(
    PermStep(
        icon       = Icons.Outlined.CameraAlt,
        title      = "Доступ к камере",
        shortTitle = "камере",
        desc       = "Нужен для сканирования документов. Фото снимаются и обрабатываются локально — ничего не уходит в сеть.",
    ),
    PermStep(
        icon       = Icons.Outlined.PhotoLibrary,
        title      = "Доступ к галерее",
        shortTitle = "галерее",
        desc       = "Чтобы сканировать фото, уже сохранённые на устройстве. Вы выбираете, какие файлы открыть.",
    ),
    PermStep(
        icon       = Icons.Outlined.FolderOpen,
        title      = "Доступ к файлам",
        shortTitle = "файлам",
        desc       = "Нужен для экспорта готовых PDF и изображений в другие приложения.",
    ),
)

@Composable
fun PermissionsScreen(
    onNavigateToHome: () -> Unit,
) {
    var stage      by rememberSaveable { mutableIntStateOf(0) }
    var showDialog by rememberSaveable { mutableStateOf(false) }

    val step = STEPS[stage]

    fun advance() { if (stage < STEPS.lastIndex) stage++ else onNavigateToHome() }

    // Launchers создаются безусловно (правило Compose), каждый для своего разрешения
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { advance() }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { advance() }

    fun onDialogConfirm() {
        showDialog = false
        when (stage) {
            0 -> cameraLauncher.launch(Manifest.permission.CAMERA)
            1 -> galleryLauncher.launch(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    Manifest.permission.READ_MEDIA_IMAGES
                else
                    Manifest.permission.READ_EXTERNAL_STORAGE
            )
            else -> advance()   // Шаг 3: реального разрешения не требует
        }
    }

    // ── Основной экран ────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPrimary),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp, start = 32.dp, end = 32.dp, bottom = 40.dp),
        ) {
            // "ШАГ 1 ИЗ 3"
            Text(
                text  = "ШАГ ${stage + 1} ИЗ ${STEPS.size}",
                style = TextStyle(
                    fontFamily    = FontMono,
                    fontWeight    = FontWeight.Normal,
                    fontSize      = 11.sp,
                    letterSpacing = 0.15.em,
                    color         = TextTertiary,
                ),
            )

            Spacer(Modifier.height(32.dp))

            // Иконка в скруглённом квадрате AccentSubtle
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(AccentSubtle),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector        = step.icon,
                    contentDescription = null,
                    modifier           = Modifier.size(38.dp),
                    tint               = Accent,
                )
            }

            Spacer(Modifier.height(32.dp))

            // Заголовок — Cormorant Garamond SemiBold 30sp
            Text(
                text  = step.title,
                style = TextStyle(
                    fontFamily    = FontDisplay,
                    fontWeight    = FontWeight.SemiBold,
                    fontSize      = 30.sp,
                    letterSpacing = 0.01.em,
                    color         = TextPrimary,
                ),
            )

            Spacer(Modifier.height(16.dp))

            // Описание — DM Sans Light 16sp
            Text(
                text  = step.desc,
                style = TextStyle(
                    fontFamily = FontBody,
                    fontWeight = FontWeight.Light,
                    fontSize   = 16.sp,
                    lineHeight = (16 * 1.55).sp,
                    color      = TextSecondary,
                ),
            )

            Spacer(Modifier.weight(1f))

            // Кнопка "Разрешить"
            PrimaryButton(
                text     = "Разрешить",
                onClick  = { showDialog = true },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(8.dp))

            // Кнопка "Не сейчас" — ghost стиль
            TextButton(
                onClick  = { advance() },
                modifier = Modifier.fillMaxWidth(),
                colors   = ButtonDefaults.textButtonColors(contentColor = TextSecondary),
            ) {
                Text(
                    text  = "Не сейчас",
                    style = TextStyle(
                        fontFamily = FontBody,
                        fontWeight = FontWeight.Normal,
                        fontSize   = 16.sp,
                    ),
                )
            }
        }

        // ── Диалог подтверждения ──────────────────────────────────────────────
        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Surface),
                ) {
                    Column(
                        modifier            = Modifier.padding(
                            start  = 20.dp,
                            end    = 20.dp,
                            top    = 24.dp,
                            bottom = 16.dp,
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text      = "Разрешить «Архиву» доступ к ${step.shortTitle}?",
                            style     = TextStyle(
                                fontFamily = FontBody,
                                fontWeight = FontWeight.SemiBold,
                                fontSize   = 17.sp,
                            ),
                            color     = TextPrimary,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text      = "Приложение использует данные только локально.",
                            style     = TextStyle(
                                fontFamily = FontBody,
                                fontWeight = FontWeight.Light,
                                fontSize   = 13.sp,
                                lineHeight = (13 * 1.4).sp,
                                color      = TextSecondary,
                            ),
                            textAlign = TextAlign.Center,
                        )
                    }

                    HorizontalDivider(color = BgTertiary, thickness = 1.dp)

                    Row(modifier = Modifier.fillMaxWidth()) {
                        // "Запретить"
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { showDialog = false }
                                .padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text  = "Запретить",
                                style = TextStyle(
                                    fontFamily = FontBody,
                                    fontWeight = FontWeight.Normal,
                                    fontSize   = 15.sp,
                                    color      = TextSecondary,
                                ),
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(width = 1.dp, height = 48.dp)
                                .background(BgTertiary)
                                .align(Alignment.CenterVertically),
                        )

                        // "Разрешить"
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onDialogConfirm() }
                                .padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text  = "Разрешить",
                                style = TextStyle(
                                    fontFamily = FontBody,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize   = 15.sp,
                                    color      = Accent,
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}
