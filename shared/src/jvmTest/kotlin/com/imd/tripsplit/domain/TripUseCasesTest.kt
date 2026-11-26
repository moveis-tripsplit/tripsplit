package com.imd.tripsplit.domain

import com.imd.tripsplit.data.repository.InMemoryTripRepository
import com.imd.tripsplit.domain.model.TripActivity
import com.imd.tripsplit.domain.model.TripDraft
import com.imd.tripsplit.domain.model.TripStatus
import com.imd.tripsplit.domain.usecase.CreateTripUseCase
import com.imd.tripsplit.domain.usecase.GetTripUseCase
import com.imd.tripsplit.domain.usecase.UpdateTripUseCase
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class TripUseCasesTest {
    @Test
    fun createTripPreservesCreationRules() = runBlocking {
        val repository = InMemoryTripRepository()
        val createTrip = CreateTripUseCase(repository)
        val draft = TripDraft(
            title = "  Rio  ",
            description = "  Férias  ",
            startDate = "2026-12-01",
            endDate = "2026-12-10",
            participants = emptyList(),
            activities = listOf(
                TripActivity("a1", "Praia", "09:00", "Copacabana", 25.0),
                TripActivity("a2", "Museu", "14:00", "Centro"),
            ),
        )

        val created = createTrip(draft)

        assertTrue(created.id.startsWith("trip-"))
        assertEquals("Rio", created.title)
        assertEquals("Rio", created.destination)
        assertEquals("Férias", created.description)
        assertEquals(TripStatus.UPCOMING, created.status)
        assertEquals(25.0, created.totalAmount)
        assertEquals(0.0, created.myBalance)
        assertEquals(created, GetTripUseCase(repository)(created.id))
    }

    @Test
    fun updateTripChangesOnlyEditableFields() = runBlocking {
        val repository = InMemoryTripRepository()
        val createTrip = CreateTripUseCase(repository)
        val original = createTrip(
            TripDraft("Roma", "", "2026-12-01", "2026-12-10", emptyList(), emptyList()),
        ).copy(
            destination = "Roma, Itália",
            status = TripStatus.ONGOING,
            totalAmount = 120.0,
            myBalance = 35.0,
        )
        repository.updateTrip(original)

        val updated = UpdateTripUseCase(repository)(
            original,
            TripDraft("  Milão  ", "  Alterada  ", "2027-01-01", "2027-01-05", emptyList(), emptyList()),
        )

        assertEquals("Milão", updated.title)
        assertEquals("Alterada", updated.description)
        assertEquals(original.destination, updated.destination)
        assertEquals(original.status, updated.status)
        assertEquals(original.totalAmount, updated.totalAmount)
        assertEquals(original.myBalance, updated.myBalance)
        assertNotNull(GetTripUseCase(repository)(original.id))
        assertEquals(updated, GetTripUseCase(repository)(original.id))
    }
}
