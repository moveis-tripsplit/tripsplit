package com.imd.tripsplit.ui.screens.home.trip.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imd.tripsplit.ui.theme.RadiusMD
import com.imd.tripsplit.ui.theme.RadiusXL
import com.imd.tripsplit.ui.theme.SpacingLG
import com.imd.tripsplit.ui.theme.SpacingMD
import com.imd.tripsplit.ui.theme.SpacingSM
import com.imd.tripsplit.ui.theme.SpacingXS

@Composable
fun CreateTripHeader(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEditing: Boolean = false,
    isCreator: Boolean = true,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Barra Superior de Navegação
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Card(
                shape = RoundedCornerShape(RadiusMD),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier =
                    Modifier
                        .size(40.dp)
                        .clickable { onBackClick() },
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "←", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                }
            }

            Text(
                text = if (!isEditing) "Cadastrar Viagem" else if (isCreator) "Editar Viagem" else "Detalhes da Viagem",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Card(
                shape = RoundedCornerShape(RadiusMD),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier =
                    Modifier
                        .size(40.dp)
                        .clickable { onBackClick() },
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "✕", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        Spacer(modifier = Modifier.height(SpacingLG))

        // Cabeçalho Informativo (só no cadastro)
        if (!isEditing) {
            Box(
                modifier =
                    Modifier
                        .clip(RoundedCornerShape(RadiusXL))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(horizontal = SpacingMD, vertical = SpacingXS),
            ) {
                Text(
                    text = "✈️ NOVA VIAGEM EM GRUPO",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }

            Spacer(modifier = Modifier.height(SpacingSM))
        }

        Text(
            text = if (!isEditing) "Para onde vamos?" else if (isCreator) "Editar viagem" else "Detalhes da viagem",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.height(SpacingXS))

        Text(
            text =
                if (!isEditing) {
                    "Preencha as informações da viagem para começar a dividir gastos com os participantes."
                } else if (isCreator) {
                    "Atualize as informações, o roteiro e os participantes da viagem."
                } else {
                    "Visualize as informações, o roteiro e os participantes da viagem."
                },
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 18.sp,
        )
    }
}
