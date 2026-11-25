package com.imd.tripsplit.ui.screens.home.trip.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imd.tripsplit.ui.theme.BlueOcean
import com.imd.tripsplit.ui.theme.BorderLight
import com.imd.tripsplit.ui.theme.RadiusMD
import com.imd.tripsplit.ui.theme.RadiusXL
import com.imd.tripsplit.ui.theme.SpacingMD
import com.imd.tripsplit.ui.theme.SpacingXL
import com.imd.tripsplit.ui.theme.TextSecondary

@Composable
fun CreateTripBottomBar(
    isLoading: Boolean,
    onSubmitClick: () -> Unit,
    onSaveDraftClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEditing: Boolean = false,
) {
    Card(
        shape = RoundedCornerShape(topStart = RadiusXL, topEnd = RadiusXL),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, BorderLight),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = SpacingXL, vertical = SpacingMD),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = onSubmitClick,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                shape = RoundedCornerShape(RadiusMD),
                colors = ButtonDefaults.buttonColors(containerColor = BlueOcean),
                enabled = !isLoading,
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                    )
                } else {
                    Text(
                        text = if (isEditing) "Salvar alterações" else "Criar Viagem ✈️",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }
            }

            if (!isEditing) {
                TextButton(onClick = onSaveDraftClick) {
                    Text(
                        text = "Salvar como rascunho",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary,
                    )
                }
            }
        }
    }
}
