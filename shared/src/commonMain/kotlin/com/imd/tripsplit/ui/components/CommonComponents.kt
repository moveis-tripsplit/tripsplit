package com.imd.tripsplit.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.imd.tripsplit.model.Participant
import com.imd.tripsplit.model.TripStatus
import com.imd.tripsplit.ui.theme.RadiusLG
import com.imd.tripsplit.ui.theme.RadiusMD
import com.imd.tripsplit.ui.theme.SpacingMD
import com.imd.tripsplit.ui.theme.SpacingSM
import com.imd.tripsplit.ui.theme.SpacingXS
import kotlin.math.abs
import kotlin.math.roundToLong

/** Formata em reais (R$ 1.234,56) sem depender de APIs de JVM, para funcionar em commonMain. */
fun Double.toBrl(): String {
    val cents = (abs(this) * 100).roundToLong()
    val reais = (cents / 100).toString().reversed().chunked(3).joinToString(".").reversed()
    val rest = (cents % 100).toString().padStart(2, '0')
    val sign = if (this < 0) "-" else ""
    return "${sign}R$ $reais,$rest"
}

/* Cores vêm sempre de MaterialTheme.colorScheme (definido em ui/theme/Theme.kt),
 * o que mantém o Dark Mode funcionando (Design System, seções 19 e 20.1). */

/* ------------------------------------------------------------------ */
/* Cartão base (Surface, raio 16dp, borda sutil)                       */
/* ------------------------------------------------------------------ */
@Composable
fun TripSplitCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    val border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    val shape = RoundedCornerShape(RadiusLG)
    if (onClick != null) {
        Card(onClick = onClick, modifier = modifier, shape = shape, colors = colors, border = border) { content() }
    } else {
        Card(modifier = modifier, shape = shape, colors = colors, border = border) { content() }
    }
}

/* ------------------------------------------------------------------ */
/* Chip de status                                                      */
/* ------------------------------------------------------------------ */
private fun TripStatus.displayName(): String = when (this) {
    TripStatus.ONGOING -> "Em andamento"
    TripStatus.UPCOMING -> "Futura"
    TripStatus.FINISHED -> "Finalizada"
}

@Composable
fun StatusChip(status: TripStatus, modifier: Modifier = Modifier) {
    val scheme = MaterialTheme.colorScheme
    val tint = when (status) {
        TripStatus.ONGOING -> scheme.tertiary // laranja: viagem
        TripStatus.UPCOMING -> scheme.primary // azul: informação
        TripStatus.FINISHED -> scheme.secondary // verde: concluído
    }
    Text(
        text = status.displayName(),
        style = MaterialTheme.typography.labelSmall,
        color = tint,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(tint.copy(alpha = 0.12f))
            .padding(horizontal = SpacingMD, vertical = SpacingXS),
    )
}

/* ------------------------------------------------------------------ */
/* Avatares empilhados dos participantes                               */
/* ------------------------------------------------------------------ */
private fun Participant.initials(): String =
    name.trim().split(" ")
        .filter { it.isNotEmpty() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

@Composable
fun Avatar(participant: Participant, modifier: Modifier = Modifier, size: Int = 28) {
    val scheme = MaterialTheme.colorScheme
    val background = scheme.primaryContainer
    val textColor = scheme.onPrimaryContainer
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(background)
            .border(2.dp, scheme.surface, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = participant.initials(),
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
        )
    }
}

@Composable
fun AvatarStack(
    participants: List<Participant>,
    modifier: Modifier = Modifier,
    maxVisible: Int = 4,
) {
    val visible = participants.take(maxVisible)
    val extra = participants.size - visible.size
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.width((28 + (visible.size - 1).coerceAtLeast(0) * 20).dp)) {
            visible.forEachIndexed { index, p ->
                Avatar(p, modifier = Modifier.offset(x = (index * 20).dp))
            }
        }
        if (extra > 0) {
            Text(
                text = "+$extra",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = SpacingSM),
            )
        }
    }
}

/* ------------------------------------------------------------------ */
/* Saldo do usuário na viagem (verde = receber, vermelho = dívida)     */
/* ------------------------------------------------------------------ */
@Composable
fun BalanceLabel(balance: Double, modifier: Modifier = Modifier) {
    val scheme = MaterialTheme.colorScheme
    val (text, color) = when {
        balance > 0 -> "Você recebe ${balance.toBrl()}" to scheme.secondary
        balance < 0 -> "Você deve ${(-balance).toBrl()}" to scheme.error
        else -> "Tudo quitado" to scheme.onSurfaceVariant
    }
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
        color = color,
        modifier = modifier,
    )
}

/* ------------------------------------------------------------------ */
/* Cabeçalho de seção                                                  */
/* ------------------------------------------------------------------ */
@Composable
fun SectionHeader(title: String, modifier: Modifier = Modifier, subtitle: String? = null) {
    Column(modifier = modifier) {
        Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
        if (subtitle != null) {
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/* ------------------------------------------------------------------ */
/* Item de estatística (usado no resumo)                               */
/* ------------------------------------------------------------------ */
@Composable
fun StatItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Column(modifier = modifier) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.titleMedium, color = valueColor)
    }
}

/* ------------------------------------------------------------------ */
/* Botões: texto sempre onPrimary/onTertiary (branco), estados         */
/* Default / Disabled / Loading (Design System, seção 8.5)             */
/* ------------------------------------------------------------------ */
@Composable
private fun TripSplitButton(
    text: String,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier,
    enabled: Boolean,
    loading: Boolean,
) {
    Button(
        onClick = { if (!loading) onClick() },
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(RadiusMD),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = MaterialTheme.colorScheme.outline,
            disabledContentColor = MaterialTheme.colorScheme.outlineVariant,
        ),
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = LocalContentColor.current,
                strokeWidth = 2.dp,
            )
        } else {
            Text(text, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold))
        }
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    TripSplitButton(
        text = text,
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = modifier,
        enabled = enabled,
        loading = loading,
    )
}

@Composable
fun AccentButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    TripSplitButton(
        text = text,
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.tertiary,
        contentColor = MaterialTheme.colorScheme.onTertiary,
        modifier = modifier,
        enabled = enabled,
        loading = loading,
    )
}