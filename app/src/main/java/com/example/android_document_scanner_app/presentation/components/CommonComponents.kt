package com.example.android_document_scanner_app.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_document_scanner_app.presentation.theme.Accent
import com.example.android_document_scanner_app.presentation.theme.AccentSubtle
import com.example.android_document_scanner_app.presentation.theme.BgPrimary
import com.example.android_document_scanner_app.presentation.theme.BgSecondary
import com.example.android_document_scanner_app.presentation.theme.BgTertiary
import com.example.android_document_scanner_app.presentation.theme.DocScannerTheme
import com.example.android_document_scanner_app.presentation.theme.FontBody
import com.example.android_document_scanner_app.presentation.theme.FontDisplay
import com.example.android_document_scanner_app.presentation.theme.MonoSmall
import com.example.android_document_scanner_app.presentation.theme.SurfaceRaised
import com.example.android_document_scanner_app.presentation.theme.TextPrimary
import com.example.android_document_scanner_app.presentation.theme.TextSecondary
import com.example.android_document_scanner_app.presentation.theme.TextTertiary

// ── 1. PrimaryButton ─────────────────────────────────────────────────────────

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor         = Accent,
            contentColor           = Color.White,
            disabledContainerColor = BgTertiary,
            disabledContentColor   = TextTertiary,
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                fontFamily    = FontBody,
                fontWeight    = FontWeight.Medium,
                letterSpacing = 0.sp,
            ),
        )
    }
}

// ── 2. SecondaryButton ───────────────────────────────────────────────────────

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, if (enabled) Accent else BgTertiary),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor   = Color.Transparent,
            contentColor     = Accent,
            disabledContentColor = TextTertiary,
        ),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                fontFamily    = FontBody,
                fontWeight    = FontWeight.Medium,
                letterSpacing = 0.sp,
            ),
        )
    }
}

// ── 3. DocScannerTopBar ──────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocScannerTopBar(
    title: String,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = FontDisplay,
                    fontWeight = FontWeight.SemiBold,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад",
                        tint               = TextPrimary,
                    )
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor             = Color.Transparent,
            titleContentColor          = TextPrimary,
            navigationIconContentColor = TextPrimary,
            actionIconContentColor     = TextPrimary,
        ),
    )
}

// ── 4. StatusChip ────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        selected = selected,
        onClick  = onClick,
        label = {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontFamily = FontBody,
                    fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
                ),
            )
        },
        modifier = modifier,
        shape    = RoundedCornerShape(8.dp),
        colors   = FilterChipDefaults.filterChipColors(
            selectedContainerColor = AccentSubtle,
            selectedLabelColor     = Accent,
            containerColor         = BgSecondary,
            labelColor             = TextSecondary,
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled             = true,
            selected            = selected,
            borderColor         = BgTertiary,
            selectedBorderColor = Accent,
            borderWidth         = 1.dp,
            selectedBorderWidth = 1.dp,
        ),
    )
}

// ── 5. DocumentCard ──────────────────────────────────────────────────────────

@Composable
fun DocumentCard(
    name: String,
    date: String,
    pageCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    thumbnail: (@Composable BoxScope.() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation    = 4.dp,
                shape        = RoundedCornerShape(12.dp),
                clip         = false, // тень должна выходить за границы, клип — отдельно
                ambientColor = Accent.copy(alpha = 0.06f),
                spotColor    = Accent.copy(alpha = 0.10f),
            )
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceRaised)
            .clickable(onClick = onClick)
            .padding(12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Миниатюра: 56×72dp, скруглённые углы
            Box(
                modifier = Modifier
                    .size(width = 56.dp, height = 72.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(BgSecondary),
                contentAlignment = Alignment.Center,
            ) {
                if (thumbnail != null) {
                    thumbnail()
                } else {
                    Text(
                        text  = "DOC",
                        style = MonoSmall.copy(color = TextTertiary),
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text  = name,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = FontBody,
                        fontWeight = FontWeight.Medium,
                        fontSize   = 15.sp,
                        lineHeight = 20.sp,
                        color      = TextPrimary,
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text  = date,
                    style = MonoSmall.copy(color = TextSecondary),
                )
                if (pageCount > 1) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text  = "$pageCount стр.",
                        style = MonoSmall.copy(color = TextTertiary),
                    )
                }
            }
        }
    }
}

// ── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFFF7F4EF)
@Composable
private fun PrimaryButtonPreview() {
    DocScannerTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            PrimaryButton("Сохранить документ", onClick = {}, modifier = Modifier.fillMaxWidth())
            PrimaryButton("Недоступно", onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F4EF)
@Composable
private fun SecondaryButtonPreview() {
    DocScannerTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            SecondaryButton("Переснять", onClick = {}, modifier = Modifier.fillMaxWidth())
            SecondaryButton("Недоступно", onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F4EF)
@Composable
private fun DocScannerTopBarPreview() {
    DocScannerTheme {
        Column {
            DocScannerTopBar(title = "Мои документы")
            HorizontalDivider()
            DocScannerTopBar(title = "Редактирование", onBack = {})
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F4EF)
@Composable
private fun StatusChipPreview() {
    DocScannerTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            StatusChip("PDF", selected = true,  onClick = {})
            StatusChip("JPG", selected = false, onClick = {})
            StatusChip("PNG", selected = false, onClick = {})
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F4EF)
@Composable
private fun DocumentCardPreview() {
    DocScannerTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            DocumentCard(
                name      = "Договор аренды офиса 2026.pdf",
                date      = "25 апр 2026",
                pageCount = 3,
                onClick   = {},
            )
            DocumentCard(
                name      = "Паспорт",
                date      = "24 апр 2026",
                pageCount = 1,
                onClick   = {},
            )
        }
    }
}
