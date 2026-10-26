package com.imd.tripsplit.ui.screens.home.trip.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imd.tripsplit.ui.screens.home.trip.TripActivity
import com.imd.tripsplit.ui.theme.RadiusLG
import com.imd.tripsplit.ui.theme.RadiusMD
import com.imd.tripsplit.ui.theme.RadiusXL
import com.imd.tripsplit.ui.theme.SpacingLG
import com.imd.tripsplit.ui.theme.SpacingMD
import com.imd.tripsplit.ui.theme.SpacingSM
import com.imd.tripsplit.ui.theme.SpacingXS
import com.imd.tripsplit.ui.theme.TextSecondary

@Composable
fun TripActivitiesCard(
    activities: List<TripActivity>,
    onAddActivityClick: () -> Unit,
    onRemoveActivity: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(RadiusLG),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(SpacingLG)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Roteiro de Atividades",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.outlineVariant,
                )

                Box(
                    modifier =
                        Modifier
                            .clip(RoundedCornerShape(RadiusXL))
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(RadiusXL))
                            .padding(horizontal = SpacingSM, vertical = 2.dp),
                ) {
                    Text(
                        text = "Pode adicionar depois",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }
            }

            Spacer(modifier = Modifier.height(SpacingXS))

            Text(
                text = "Atividades já programadas para a viagem:",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(SpacingSM))

            if (activities.isEmpty()) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(RadiusMD))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant), RoundedCornerShape(RadiusMD))
                            .padding(SpacingMD),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Nenhuma atividade adicionada ainda",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Você pode salvar agora e criar as atividades depois.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        )
                    }
                }
            } else {
                activities.forEach { activity ->
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(RadiusMD))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(RadiusMD))
                                .padding(horizontal = SpacingMD, vertical = SpacingSM),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(activity.dateHour, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                Text(activity.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("📍 ${activity.location}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (activity.estimatedCost != null) {
                                    Text(
                                        text = "R$ ${activity.estimatedCost.toInt()}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.tertiary,
                                    )
                                    Spacer(modifier = Modifier.width(SpacingSM))
                                }
                                IconButton(
                                    onClick = { onRemoveActivity(activity.id) },
                                    modifier = Modifier.size(24.dp),
                                ) {
                                    Text("✕", fontSize = 12.sp, color = TextSecondary)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(SpacingSM))

            Button(
                onClick = onAddActivityClick,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(RadiusMD),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "+ Adicionar atividade ao roteiro",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Spacer(modifier = Modifier.height(SpacingXS))

            Text(
                text = "💡 Dica: Não definiu todos os passeios? Adicione durante a viagem.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            )
        }
    }
}
