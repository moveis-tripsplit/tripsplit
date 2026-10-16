package com.imd.tripsplit.ui.screens.trip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imd.tripsplit.ui.screens.trip.components.CreateTripBottomBar
import com.imd.tripsplit.ui.screens.trip.components.CreateTripHeader
import com.imd.tripsplit.ui.screens.trip.components.TripActivitiesCard
import com.imd.tripsplit.ui.screens.trip.components.TripDatesCard
import com.imd.tripsplit.ui.screens.trip.components.TripInfoCard
import com.imd.tripsplit.ui.screens.trip.components.TripParticipantsCard
import com.imd.tripsplit.ui.theme.SpacingLG
import com.imd.tripsplit.ui.theme.SpacingMD
import com.imd.tripsplit.ui.theme.SpacingXL

@Composable
fun CreateTripContent(
    state: CreateTripUiState,
    onAction: (CreateTripAction) -> Unit,
    onSubmitClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEditing: Boolean = false,
) {
    val scrollState = rememberScrollState()

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .imePadding()
                    .padding(horizontal = SpacingXL, vertical = SpacingLG)
                    .padding(bottom = 100.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            // 1. Barra de Navegação + Introdução
            CreateTripHeader(
                onBackClick = onBackClick,
                isEditing = isEditing,
            )

            Spacer(modifier = Modifier.height(SpacingLG))

            // 2. Informações Gerais (Título e Descrição)
            TripInfoCard(
                title = state.title,
                description = state.description,
                onTitleChange = { onAction(CreateTripAction.UpdateTitle(it)) },
                onDescriptionChange = { onAction(CreateTripAction.UpdateDescription(it)) },
            )

            Spacer(modifier = Modifier.height(SpacingMD))

            // 3. Período e Datas
            TripDatesCard(
                startDate = state.startDate,
                endDate = state.endDate,
                onStartDateChange = { onAction(CreateTripAction.UpdateStartDate(it)) },
                onEndDateChange = { onAction(CreateTripAction.UpdateEndDate(it)) },
            )

            Spacer(modifier = Modifier.height(SpacingMD))

            // 4. Roteiro de Atividades
            TripActivitiesCard(
                activities = state.activities,
                onAddActivityClick = { onAction(CreateTripAction.OpenAddActivityModal) },
                onRemoveActivity = { onAction(CreateTripAction.RemoveActivity(it)) },
            )

            Spacer(modifier = Modifier.height(SpacingMD))

            // 5. Participantes
            TripParticipantsCard(
                participants = state.participants,
                onAddParticipantClick = { onAction(CreateTripAction.OpenAddParticipantModal) },
                onRemoveParticipant = { onAction(CreateTripAction.RemoveParticipant(it)) },
            )
        }

        // 6. Rodapé Fixo Inferior
        CreateTripBottomBar(
            isLoading = state.isLoading,
            onSubmitClick = onSubmitClick,
            onSaveDraftClick = onBackClick,
            isEditing = isEditing,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}
