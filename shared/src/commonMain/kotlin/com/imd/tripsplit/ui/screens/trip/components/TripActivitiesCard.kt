package com.imd.tripsplit.ui.screens.trip.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imd.tripsplit.ui.screens.trip.TripActivity
import com.imd.tripsplit.ui.theme.BlueOcean
import com.imd.tripsplit.ui.theme.BorderLight
import com.imd.tripsplit.ui.theme.GreenTravel
import com.imd.tripsplit.ui.theme.OrangeAccent
import com.imd.tripsplit.ui.theme.RadiusLG
import com.imd.tripsplit.ui.theme.RadiusMD
import com.imd.tripsplit.ui.theme.RadiusXL
import com.imd.tripsplit.ui.theme.SpacingLG
import com.imd.tripsplit.ui.theme.SpacingMD
import com.imd.tripsplit.ui.theme.SpacingSM
import com.imd.tripsplit.ui.theme.SpacingXS
import com.imd.tripsplit.ui.theme.TextDisabled
import com.imd.tripsplit.ui.theme.TextPrimary
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
        border = BorderStroke(1.dp, BorderLight),
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
                    color = TextPrimary,
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(RadiusXL))
                        .background(Color(0xFFFFF7ED))
                        .border(1.dp, Color(0xFFFFEDD5), RoundedCornerShape(RadiusXL))
                        .padding(horizontal = SpacingSM, vertical = 2.dp),
                ) {
                    Text(
                        text = "Pode adicionar depois",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OrangeAccent,
                    )
                }
            }

            Spacer(modifier = Modifier.height(SpacingXS))

            Text(
                text = "Atividades já programadas para a viagem:",
                fontSize = 12.sp,
                color = TextSecondary,
            )

            Spacer(modifier = Modifier.height(SpacingSM))

            if (activities.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(RadiusMD))
                        .background(Color(0xFFF8FAFC))
                        .border(BorderStroke(1.dp, BorderLight), RoundedCornerShape(RadiusMD))
                        .padding(SpacingMD),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Nenhuma atividade adicionada ainda",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondary,
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Você pode salvar agora e criar as atividades depois.",
                            fontSize = 11.sp,
                            color = TextDisabled,
                        )
                    }
                }
            } else {
                activities.forEach { activity ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(RadiusMD))
                            .background(Color(0xFFF8FAFC))
                            .border(1.dp, BorderLight, RoundedCornerShape(RadiusMD))
                            .padding(horizontal = SpacingMD, vertical = SpacingSM),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(activity.dateHour, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BlueOcean)
                                Text(activity.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text("📍 ${activity.location}", fontSize = 11.sp, color = TextSecondary)
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (activity.estimatedCost != null) {
                                    Text(
                                        text = "R$ ${activity.estimatedCost.toInt()}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GreenTravel,
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
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEFF6FF),
                    contentColor = BlueOcean,
                ),
                border = BorderStroke(1.dp, BlueOcean),
                shape = RoundedCornerShape(RadiusMD),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("+ Adicionar atividade ao roteiro", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(SpacingXS))

            Text(
                text = "💡 Dica: Não definiu todos os passeios? Adicione durante a viagem.",
                fontSize = 11.sp,
                color = TextSecondary,
            )
        }
    }
}
