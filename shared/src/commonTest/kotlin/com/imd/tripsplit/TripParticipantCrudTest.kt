package com.imd.tripsplit

import com.imd.tripsplit.data.LocalDataManager
import com.imd.tripsplit.data.TripRepository
import com.imd.tripsplit.data.remote.ParticipantApiClient
import com.imd.tripsplit.data.remote.dto.ParticipantRequestDto
import com.imd.tripsplit.data.toParticipant
import com.imd.tripsplit.data.toTripParticipant
import com.imd.tripsplit.domain.TripParticipantValidator
import com.imd.tripsplit.model.Participant
import com.imd.tripsplit.model.Trip
import com.imd.tripsplit.model.TripStatus
import com.imd.tripsplit.ui.screens.home.trip.CreateTripAction
import com.imd.tripsplit.ui.screens.home.trip.CreateTripUiState
import com.imd.tripsplit.ui.screens.home.trip.TripParticipant
import com.imd.tripsplit.ui.screens.home.trip.reduce
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class TripParticipantCrudTest {

    private fun createSampleTrip(
        id: String = "trip-test-1",
        organizerCount: Int = 1,
        participantCount: Int = 2,
    ): Trip {
        val participants = mutableListOf<Participant>()
        for (i in 1..organizerCount) {
            participants.add(
                Participant(
                    id = "org-$i",
                    name = "Organizador $i",
                    role = "Organizador",
                    isOrganizer = true,
                    hasAccount = true,
                    email = "org$i@test.com",
                )
            )
        }
        for (i in 1..participantCount) {
            participants.add(
                Participant(
                    id = "part-$i",
                    name = "Participante $i",
                    role = "Participante",
                    isOrganizer = false,
                    hasAccount = false,
                    contactOrPix = "pix-$i",
                )
            )
        }
        return Trip(
            id = id,
            title = "Viagem para Salvador",
            destination = "Salvador",
            status = TripStatus.UPCOMING,
            totalAmount = 1500.0,
            myBalance = 0.0,
            startDate = "2026-12-01",
            endDate = "2026-12-05",
            participants = participants,
        )
    }

    // --- REGRAS DE NEGÓCIO (RN01, RN02, RN03) ---

    @Test
    fun testRN01_cannotDeleteSoleOrganizer() {
        val trip = createSampleTrip(organizerCount = 1, participantCount = 1)
        val soleOrganizerId = "org-1"

        val result = TripParticipantValidator.validateDeletion(trip, soleOrganizerId)
        assertTrue(result.isFailure, "Não deve permitir excluir o único organizador.")
        assertTrue(result.exceptionOrNull()?.message?.contains("pelo menos um Organizador") == true)
    }

    @Test
    fun testRN01_canDeleteOrganizerIfAnotherExists() {
        val trip = createSampleTrip(organizerCount = 2, participantCount = 1)
        val result = TripParticipantValidator.validateDeletion(trip, "org-1")
        assertTrue(result.isSuccess, "Deve permitir excluir organizador quando existe outro.")
    }

    @Test
    fun testRN01_cannotDemoteSoleOrganizer() {
        val trip = createSampleTrip(organizerCount = 1, participantCount = 1)
        val result = TripParticipantValidator.validateRoleChange(
            trip = trip,
            participantId = "org-1",
            newRoleIsOrganizer = false,
        )
        assertTrue(result.isFailure, "Não deve permitir rebaixar único organizador a participante.")
    }

    @Test
    fun testRN02_cannotDeleteParticipantWithFinancialPending() {
        val trip = createSampleTrip(organizerCount = 1, participantCount = 1)
        val result = TripParticipantValidator.validateDeletion(
            trip = trip,
            participantId = "part-1",
            hasFinancialPending = true,
        )
        assertTrue(result.isFailure, "Não deve permitir excluir participante com pendências financeiras.")
        assertTrue(result.exceptionOrNull()?.message?.contains("despesas ativas") == true)
    }

    @Test
    fun testRN03_participantDataValidation() {
        // Usuário sem conta: nome obrigatório >= 2 caracteres
        val invalidGuest = TripParticipantValidator.validateParticipantData(
            name = "A",
            email = null,
            hasAccount = false,
        )
        assertTrue(invalidGuest.isFailure)

        val validGuest = TripParticipantValidator.validateParticipantData(
            name = "Carlos Souza",
            email = null,
            hasAccount = false,
        )
        assertTrue(validGuest.isSuccess)

        // Usuário com conta: e-mail obrigatório e válido
        val invalidAccount = TripParticipantValidator.validateParticipantData(
            name = "Carlos",
            email = "invalido",
            hasAccount = true,
        )
        assertTrue(invalidAccount.isFailure)

        val validAccount = TripParticipantValidator.validateParticipantData(
            name = "Carlos",
            email = "carlos@email.com",
            hasAccount = true,
        )
        assertTrue(validAccount.isSuccess)
    }

    // --- MVI REDUCER ---

    @Test
    fun testMviReducer_crudFlow() {
        var state = CreateTripUiState()

        val p1 = TripParticipant(
            id = "p-1",
            name = "Ana Paula",
            initials = "AP",
            role = "Organizador",
            isOrganizer = true,
            hasAccount = true,
            email = "ana@email.com",
        )
        val p2 = TripParticipant(
            id = "p-2",
            name = "Lucas Mendes",
            initials = "LM",
            role = "Participante",
            isOrganizer = false,
            hasAccount = false,
            contactOrPix = "11999990000",
        )

        // 1. Adicionar participantes
        state = state.reduce(CreateTripAction.AddParticipant(p1))
        state = state.reduce(CreateTripAction.AddParticipant(p2))
        assertEquals(2, state.participants.size)

        // 2. Tentar excluir único organizador -> deve bloquear com erro
        state = state.reduce(CreateTripAction.ConfirmDeleteParticipant("p-1"))
        assertEquals(2, state.participants.size, "Não deve excluir único organizador")
        assertNotNull(state.participantErrorMessage)

        // 3. Excluir participante comum -> sucesso
        state = state.reduce(CreateTripAction.ConfirmDeleteParticipant("p-2"))
        assertEquals(1, state.participants.size)
        assertEquals("p-1", state.participants.first().id)

        // 4. Editar participante
        val updatedP1 = p1.copy(name = "Ana Paula Silva")
        state = state.reduce(CreateTripAction.UpdateParticipant(updatedP1))
        assertEquals("Ana Paula Silva", state.participants.first().name)
    }

    // --- REPOSITÓRIO & LOCALDATAMANAGER OFFLINE ---

    @Test
    fun testLocalDataManager_atomicParticipantOperations() {
        val tripId = "trip-offline-${kotlin.random.Random.nextInt(1000, 9999)}"
        val trip = createSampleTrip(id = tripId, organizerCount = 1, participantCount = 0)
        LocalDataManager.addTrip(trip)

        // Adicionar participante
        val newGuest = Participant(
            id = "part-guest",
            name = "Visitante",
            role = "Participante",
            hasAccount = false,
            contactOrPix = "pix-guest",
        )
        val addResult = LocalDataManager.addParticipantToTrip(tripId, newGuest)
        assertTrue(addResult.isSuccess)

        val retrievedParticipants = LocalDataManager.getTripParticipants(tripId)
        assertEquals(2, retrievedParticipants.size)

        // Atualizar participante
        val updatedGuest = newGuest.copy(name = "Visitante Atualizado", contactOrPix = "pix-novo")
        val updateResult = LocalDataManager.updateParticipantInTrip(tripId, updatedGuest)
        assertTrue(updateResult.isSuccess)
        assertEquals("Visitante Atualizado", LocalDataManager.getTripParticipants(tripId).find { it.id == "part-guest" }?.name)

        // Remover participante
        val removeResult = LocalDataManager.removeParticipantFromTrip(tripId, "part-guest")
        assertTrue(removeResult.isSuccess)
        assertEquals(1, LocalDataManager.getTripParticipants(tripId).size)
    }

    // --- KTOR CLIENT COM MOCK ENGINE ---

    @Test
    fun testKtorClient_httpMethods() = runTest {
        val mockEngine = MockEngine { request ->
            val url = request.url.encodedPath
            when {
                url.endsWith("/participants") && request.method.value == "GET" -> {
                    respond(
                        content = """[{"id":"1","tripId":"t1","name":"Maria","role":"Organizador","hasAccount":true}]""",
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json"),
                    )
                }
                url.endsWith("/participants") && request.method.value == "POST" -> {
                    respond(
                        content = """{"id":"2","tripId":"t1","name":"Pedro","role":"Participante","hasAccount":false}""",
                        status = HttpStatusCode.Created,
                        headers = headersOf(HttpHeaders.ContentType, "application/json"),
                    )
                }
                url.contains("/participants/") && request.method.value == "PUT" -> {
                    respond(
                        content = """{"id":"2","tripId":"t1","name":"Pedro Editado","role":"Participante","hasAccount":false}""",
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json"),
                    )
                }
                url.contains("/participants/") && request.method.value == "DELETE" -> {
                    respond(
                        content = "",
                        status = HttpStatusCode.NoContent,
                        headers = headersOf(HttpHeaders.ContentType, "application/json"),
                    )
                }
                else -> respond(
                    content = "Not Found",
                    status = HttpStatusCode.NotFound,
                )
            }
        }

        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val apiClient = ParticipantApiClient(client = httpClient, baseUrl = "https://api.tripsplit.com")

        // 1. GET
        val getResult = apiClient.getParticipants("t1")
        assertTrue(getResult.isSuccess)
        assertEquals(1, getResult.getOrThrow().size)
        assertEquals("Maria", getResult.getOrThrow().first().name)

        // 2. POST
        val postResult = apiClient.createParticipant("t1", ParticipantRequestDto(name = "Pedro"))
        assertTrue(postResult.isSuccess)
        assertEquals("Pedro", postResult.getOrThrow().name)

        // 3. PUT
        val putResult = apiClient.updateParticipant("t1", "2", ParticipantRequestDto(name = "Pedro Editado"))
        assertTrue(putResult.isSuccess)
        assertEquals("Pedro Editado", putResult.getOrThrow().name)

        // 4. DELETE
        val deleteResult = apiClient.deleteParticipant("t1", "2")
        assertTrue(deleteResult.isSuccess)
    }

    @Test
    fun testKtorClient_networkErrorHandling() = runTest {
        val errorEngine = MockEngine {
            respond(
                content = "Internal Server Error",
                status = HttpStatusCode.InternalServerError,
            )
        }

        val httpClient = HttpClient(errorEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val apiClient = ParticipantApiClient(client = httpClient, baseUrl = "https://api.tripsplit.com")
        val result = apiClient.getParticipants("trip-err")
        assertTrue(result.isFailure, "Requisição que retorna 500 deve resultar em Failure.")
    }
}
