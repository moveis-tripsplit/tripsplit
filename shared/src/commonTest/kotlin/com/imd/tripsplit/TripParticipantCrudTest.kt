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

    @Test
    fun testParticipantWithAccount_tripAppearsInParticipantTripList() {
        val user1Email = "criador_${kotlin.random.Random.nextInt(10000, 99999)}@test.com"
        val user2Email = "participante_${kotlin.random.Random.nextInt(10000, 99999)}@test.com"

        // 1. Cadastra os dois usuários
        val user1 = LocalDataManager.registerUser(
            com.imd.tripsplit.model.User(
                id = "user-${kotlin.random.Random.nextInt(10000, 99999)}",
                name = "Criador da Viagem",
                email = user1Email,
                password = "123",
            )
        ).getOrThrow()

        val user2 = LocalDataManager.registerUser(
            com.imd.tripsplit.model.User(
                id = "user-${kotlin.random.Random.nextInt(10000, 99999)}",
                name = "Participante Convidado",
                email = user2Email,
                password = "123",
            )
        ).getOrThrow()

        // 2. User 1 loga e cria uma viagem
        LocalDataManager.login(user1Email, "123")
        val tripId = "trip-shared-${kotlin.random.Random.nextInt(10000, 99999)}"
        val trip = Trip(
            id = tripId,
            userId = user1.id,
            title = "Viagem Compartilhada",
            destination = "Praia",
            status = TripStatus.UPCOMING,
            totalAmount = 1000.0,
            myBalance = 0.0,
            startDate = "2026-12-01",
            endDate = "2026-12-05",
            participants = listOf(
                Participant(
                    id = "part-1",
                    name = "Participante Convidado",
                    email = user2Email,
                    hasAccount = true,
                    userId = user2.id,
                    role = "Participante",
                )
            ),
        )
        TripRepository.addTrip(trip)

        // 3. User 1 vê a viagem
        val user1Trips = TripRepository.getTripsForCurrentUser()
        assertTrue(user1Trips.any { it.id == tripId }, "Criador da viagem deve ver a viagem.")

        // 4. User 2 faz login
        LocalDataManager.login(user2Email, "123")
        val user2Trips = TripRepository.getTripsForCurrentUser()
        assertTrue(user2Trips.any { it.id == tripId }, "Participante com conta deve ver a viagem em sua lista de viagens.")
    }

    // --- REGRAS DE NEGÓCIO: CRIADOR É SEMPRE ORGANIZADOR E NÃO PODE SER EXCLUÍDO ---

    @Test
    fun testCreatorCannotBeDeleted_evenIfOtherOrganizersExist() {
        val creator = Participant(
            id = "creator-1",
            name = "Criador da Viagem",
            userId = "creator-user-id",
            role = "Organizador",
            isOrganizer = true,
            isCreator = true,
        )
        val secondOrganizer = Participant(
            id = "org-2",
            name = "Outro Organizador",
            userId = "user-2",
            role = "Organizador",
            isOrganizer = true,
            isCreator = false,
        )
        val trip = Trip(
            id = "trip-creator-test",
            userId = "creator-user-id",
            title = "Viagem Teste",
            destination = "Destino",
            status = TripStatus.UPCOMING,
            totalAmount = 500.0,
            myBalance = 0.0,
            startDate = "2026-12-01",
            endDate = "2026-12-05",
            participants = listOf(creator, secondOrganizer),
        )

        // Tentar excluir o criador deve falhar
        val result = TripParticipantValidator.validateDeletion(trip, creator.id)
        assertTrue(result.isFailure, "Não deve permitir excluir o criador da viagem.")
        assertEquals("O criador da viagem não pode ser excluído da viagem.", result.exceptionOrNull()?.message)

        // Excluir o outro organizador é permitido pois existem outros
        val resultOther = TripParticipantValidator.validateDeletion(trip, secondOrganizer.id)
        assertTrue(resultOther.isSuccess, "Deve permitir excluir outro organizador que não seja o criador.")
    }

    @Test
    fun testCreatorCannotHaveRoleChanged_mustAlwaysBeOrganizer() {
        val creator = Participant(
            id = "creator-1",
            name = "Criador da Viagem",
            userId = "creator-user-id",
            role = "Organizador",
            isOrganizer = true,
            isCreator = true,
        )
        val secondOrganizer = Participant(
            id = "org-2",
            name = "Segundo Organizador",
            userId = "user-2",
            role = "Organizador",
            isOrganizer = true,
            isCreator = false,
        )
        val trip = Trip(
            id = "trip-creator-role-test",
            userId = "creator-user-id",
            title = "Viagem Teste",
            destination = "Destino",
            status = TripStatus.UPCOMING,
            totalAmount = 500.0,
            myBalance = 0.0,
            startDate = "2026-12-01",
            endDate = "2026-12-05",
            participants = listOf(creator, secondOrganizer),
        )

        // Tentar rebaixar o criador para 'Participante' mesmo havendo outro organizador deve falhar
        val result = TripParticipantValidator.validateRoleChange(
            trip = trip,
            participantId = creator.id,
            newRoleIsOrganizer = false,
        )
        assertTrue(result.isFailure, "O criador não pode ter seu papel alterado para participante.")
        assertEquals("O criador da viagem sempre será o organizador e não pode ter seu papel alterado.", result.exceptionOrNull()?.message)

        // O segundo organizador pode ter o papel alterado pois o criador permanece como organizador
        val resultOther = TripParticipantValidator.validateRoleChange(
            trip = trip,
            participantId = secondOrganizer.id,
            newRoleIsOrganizer = false,
        )
        assertTrue(resultOther.isSuccess, "Outro organizador pode ter seu papel alterado.")
    }

    @Test
    fun testCreateTripUiState_reduceProtectsCreator() {
        val creator = TripParticipant(
            id = "creator-part",
            name = "Criador",
            initials = "CR",
            role = "Organizador",
            isOrganizer = true,
            isCreator = true,
        )
        val friend = TripParticipant(
            id = "friend-part",
            name = "Amigo",
            initials = "AM",
            role = "Participante",
            isOrganizer = false,
            isCreator = false,
        )

        val state = CreateTripUiState(participants = listOf(creator, friend))

        // 1. Tentar abrir exclusão para o criador bloqueia
        val deleteOpenState = state.reduce(CreateTripAction.OpenDeleteParticipantModal(creator))
        assertNotNull(deleteOpenState.participantErrorMessage)
        assertEquals("O criador da viagem não pode ser excluído da viagem.", deleteOpenState.participantErrorMessage)

        // 2. Tentar confirmar exclusão do criador bloqueia
        val deleteConfirmState = state.reduce(CreateTripAction.ConfirmDeleteParticipant(creator.id))
        assertNotNull(deleteConfirmState.participantErrorMessage)
        assertEquals("O criador da viagem não pode ser excluído da viagem.", deleteConfirmState.participantErrorMessage)

        // 3. Tentar alterar papel do criador bloqueia
        val demotedCreator = creator.copy(role = "Participante", isOrganizer = false)
        val updateState = state.reduce(CreateTripAction.UpdateParticipant(demotedCreator))
        assertNotNull(updateState.participantErrorMessage)
        assertEquals("O criador da viagem sempre será o organizador e não pode ter seu papel alterado.", updateState.participantErrorMessage)

        // 4. Remover amigo comum funciona normalmente
        val removeFriendState = state.reduce(CreateTripAction.RemoveParticipant(friend.id))
        assertEquals(1, removeFriendState.participants.size)
        assertEquals(creator.id, removeFriendState.participants.first().id)
    }

    // --- REGRAS DE AUTORIZAÇÃO: CRIADOR, ORGANIZADOR E PARTICIPANTE ---

    @Test
    fun testParticipantCannotDeleteOtherParticipants() {
        val creator = Participant(
            id = "creator-1",
            name = "Criador",
            userId = "user-creator",
            role = "Organizador",
            isOrganizer = true,
            isCreator = true,
        )
        val organizer = Participant(
            id = "org-1",
            name = "Organizador",
            userId = "user-org",
            role = "Organizador",
            isOrganizer = true,
            isCreator = false,
        )
        val part1 = Participant(
            id = "part-1",
            name = "Participante 1",
            userId = "user-p1",
            role = "Participante",
            isOrganizer = false,
            isCreator = false,
        )
        val part2 = Participant(
            id = "part-2",
            name = "Participante 2",
            userId = "user-p2",
            role = "Participante",
            isOrganizer = false,
            isCreator = false,
        )
        val trip = Trip(
            id = "trip-auth-del",
            userId = "user-creator",
            title = "Viagem de Teste",
            destination = "Destino",
            status = TripStatus.UPCOMING,
            totalAmount = 200.0,
            myBalance = 0.0,
            startDate = "2026-12-01",
            endDate = "2026-12-05",
            participants = listOf(creator, organizer, part1, part2),
        )

        // Participante comum tentando excluir outro participante -> Deve falhar
        val failResult = TripParticipantValidator.validateDeletion(
            trip = trip,
            participantId = "part-2",
            requesterUserId = "user-p1",
        )
        assertTrue(failResult.isFailure, "Participante não pode excluir outros participantes.")
        assertEquals("Apenas organizadores podem excluir participantes da viagem.", failResult.exceptionOrNull()?.message)

        // Organizador tentando excluir participante comum -> Deve ter sucesso
        val successResult = TripParticipantValidator.validateDeletion(
            trip = trip,
            participantId = "part-2",
            requesterUserId = "user-org",
        )
        assertTrue(successResult.isSuccess, "Organizador pode excluir participantes comuns.")

        // Organizador tentando excluir o criador -> Deve falhar
        val creatorFailResult = TripParticipantValidator.validateDeletion(
            trip = trip,
            participantId = "creator-1",
            requesterUserId = "user-org",
        )
        assertTrue(creatorFailResult.isFailure, "Organizador não pode excluir o criador.")
        assertEquals("O criador da viagem não pode ser excluído da viagem.", creatorFailResult.exceptionOrNull()?.message)
    }

    @Test
    fun testParticipantCannotEditOtherParticipants() {
        val creator = Participant(
            id = "creator-1",
            name = "Criador",
            userId = "user-creator",
            role = "Organizador",
            isOrganizer = true,
            isCreator = true,
        )
        val organizer = Participant(
            id = "org-1",
            name = "Organizador",
            userId = "user-org",
            role = "Organizador",
            isOrganizer = true,
            isCreator = false,
        )
        val part1 = Participant(
            id = "part-1",
            name = "Participante 1",
            userId = "user-p1",
            role = "Participante",
            isOrganizer = false,
            isCreator = false,
        )
        val part2 = Participant(
            id = "part-2",
            name = "Participante 2",
            userId = "user-p2",
            role = "Participante",
            isOrganizer = false,
            isCreator = false,
        )
        val trip = Trip(
            id = "trip-auth-edit",
            userId = "user-creator",
            title = "Viagem de Teste",
            destination = "Destino",
            status = TripStatus.UPCOMING,
            totalAmount = 200.0,
            myBalance = 0.0,
            startDate = "2026-12-01",
            endDate = "2026-12-05",
            participants = listOf(creator, organizer, part1, part2),
        )

        // Participante tentando editar outro participante -> Deve falhar
        val failResult = TripParticipantValidator.validateEdition(
            trip = trip,
            targetParticipantId = "part-2",
            requesterUserId = "user-p1",
        )
        assertTrue(failResult.isFailure, "Participante não pode editar outros viajantes.")
        assertEquals("Participantes não têm permissão para editar participantes. Apenas organizadores têm essa permissão.", failResult.exceptionOrNull()?.message)

        // Organizador tentando editar participante -> Deve ter sucesso
        val successResult = TripParticipantValidator.validateEdition(
            trip = trip,
            targetParticipantId = "part-2",
            requesterUserId = "user-org",
        )
        assertTrue(successResult.isSuccess, "Organizador pode editar participantes.")
    }

    @Test
    fun testOnlyTripCreatorCanDefineParticipantAsOrganizer() {
        val creator = Participant(
            id = "creator-1",
            name = "Criador",
            userId = "user-creator",
            role = "Organizador",
            isOrganizer = true,
            isCreator = true,
        )
        val organizer = Participant(
            id = "org-1",
            name = "Organizador Secundário",
            userId = "user-org",
            role = "Organizador",
            isOrganizer = true,
            isCreator = false,
        )
        val part1 = Participant(
            id = "part-1",
            name = "Participante 1",
            userId = "user-p1",
            role = "Participante",
            isOrganizer = false,
            isCreator = false,
        )
        val trip = Trip(
            id = "trip-auth-promote",
            userId = "user-creator",
            title = "Viagem de Teste",
            destination = "Destino",
            status = TripStatus.UPCOMING,
            totalAmount = 200.0,
            myBalance = 0.0,
            startDate = "2026-12-01",
            endDate = "2026-12-05",
            participants = listOf(creator, organizer, part1),
        )

        // Organizador comum tentando promover participante para Organizador -> Deve falhar
        val orgPromoteFail = TripParticipantValidator.validateRoleChange(
            trip = trip,
            participantId = "part-1",
            newRoleIsOrganizer = true,
            requesterUserId = "user-org",
        )
        assertTrue(orgPromoteFail.isFailure, "Organizador comum não pode promover ninguém a Organizador.")
        assertEquals("Apenas o criador da viagem pode definir participantes como organizador.", orgPromoteFail.exceptionOrNull()?.message)

        // Participante comum tentando promover participante para Organizador -> Deve falhar
        val partPromoteFail = TripParticipantValidator.validateRoleChange(
            trip = trip,
            participantId = "part-1",
            newRoleIsOrganizer = true,
            requesterUserId = "user-p1",
        )
        assertTrue(partPromoteFail.isFailure)
        assertEquals("Apenas o criador da viagem pode definir participantes como organizador.", partPromoteFail.exceptionOrNull()?.message)

        // O Criador da viagem promovendo o participante para Organizador -> Deve ter sucesso
        val creatorPromoteSuccess = TripParticipantValidator.validateRoleChange(
            trip = trip,
            participantId = "part-1",
            newRoleIsOrganizer = true,
            requesterUserId = "user-creator",
        )
        assertTrue(creatorPromoteSuccess.isSuccess, "O criador da viagem tem permissão para definir participantes como organizador.")
    }

    @Test
    fun testOnlyTripCreatorCanAddParticipants() {
        val creator = Participant(
            id = "creator-1",
            name = "Criador",
            userId = "user-creator",
            role = "Organizador",
            isOrganizer = true,
            isCreator = true,
        )
        val organizer = Participant(
            id = "org-1",
            name = "Organizador Secundário",
            userId = "user-org",
            role = "Organizador",
            isOrganizer = true,
            isCreator = false,
        )
        val trip = Trip(
            id = "trip-auth-add",
            userId = "user-creator",
            title = "Viagem de Teste",
            destination = "Destino",
            status = TripStatus.UPCOMING,
            totalAmount = 200.0,
            myBalance = 0.0,
            startDate = "2026-12-01",
            endDate = "2026-12-05",
            participants = listOf(creator, organizer),
        )

        val newGuest = Participant(
            id = "new-p",
            name = "Novo Participante",
            role = "Participante",
            isOrganizer = false,
            hasAccount = false,
        )

        // Organizador comum tentando adicionar novo participante -> Deve falhar
        val orgAddFail = TripParticipantValidator.validateAddition(
            trip = trip,
            newParticipant = newGuest,
            requesterUserId = "user-org",
        )
        assertTrue(orgAddFail.isFailure)
        assertEquals("Apenas o criador da viagem pode adicionar participantes.", orgAddFail.exceptionOrNull()?.message)

        // Participante comum tentando adicionar novo participante -> Deve falhar
        val partAddFail = TripParticipantValidator.validateAddition(
            trip = trip,
            newParticipant = newGuest,
            requesterUserId = "user-p1",
        )
        assertTrue(partAddFail.isFailure)
        assertEquals("Apenas o criador da viagem pode adicionar participantes.", partAddFail.exceptionOrNull()?.message)

        // Criador da viagem adicionando participante -> Deve ter sucesso
        val creatorAddSuccess = TripParticipantValidator.validateAddition(
            trip = trip,
            newParticipant = newGuest,
            requesterUserId = "user-creator",
        )
        assertTrue(creatorAddSuccess.isSuccess)
    }

    @Test
    fun testLocalDataManagerEnforcesRolePermissions() {
        val tripId = "trip-ldm-auth-${kotlin.random.Random.nextInt(1000, 9999)}"
        val creator = Participant(
            id = "creator-ldm",
            name = "Criador",
            userId = "user-creator-ldm",
            role = "Organizador",
            isOrganizer = true,
            isCreator = true,
        )
        val organizer = Participant(
            id = "org-ldm",
            name = "Organizador",
            userId = "user-org-ldm",
            role = "Organizador",
            isOrganizer = true,
            isCreator = false,
        )
        val part1 = Participant(
            id = "part-ldm-1",
            name = "Viajante 1",
            userId = "user-p-ldm-1",
            role = "Participante",
            isOrganizer = false,
            isCreator = false,
        )
        val trip = Trip(
            id = tripId,
            userId = "user-creator-ldm",
            title = "Viagem LDM Auth",
            destination = "Natal",
            status = TripStatus.UPCOMING,
            totalAmount = 500.0,
            myBalance = 0.0,
            startDate = "2026-12-01",
            endDate = "2026-12-05",
            participants = listOf(creator, organizer, part1),
        )
        LocalDataManager.addTrip(trip)

        // 1. Participante comum tentando excluir outro participante via LocalDataManager -> Deve falhar
        val deleteFail = LocalDataManager.removeParticipantFromTrip(
            tripId = tripId,
            participantId = "part-ldm-1",
            requesterUserId = "user-p-ldm-1",
        )
        assertTrue(deleteFail.isFailure)
        assertEquals("Apenas organizadores podem excluir participantes da viagem.", deleteFail.exceptionOrNull()?.message)

        // 2. Organizador tentando promover participante a organizador via LocalDataManager -> Deve falhar
        val updatedToOrg = part1.copy(role = "Organizador", isOrganizer = true)
        val promoteFail = LocalDataManager.updateParticipantInTrip(
            tripId = tripId,
            updated = updatedToOrg,
            requesterUserId = "user-org-ldm",
        )
        assertTrue(promoteFail.isFailure)
        assertEquals("Apenas o criador da viagem pode definir participantes como organizador.", promoteFail.exceptionOrNull()?.message)

        // 3. Criador promovendo participante a organizador -> Deve ter sucesso
        val promoteSuccess = LocalDataManager.updateParticipantInTrip(
            tripId = tripId,
            updated = updatedToOrg,
            requesterUserId = "user-creator-ldm",
        )
        assertTrue(promoteSuccess.isSuccess)

        // 4. Organizador excluindo participante -> Deve ter sucesso (já que agora temos 3 organizadores)
        val deleteSuccess = LocalDataManager.removeParticipantFromTrip(
            tripId = tripId,
            participantId = "part-ldm-1",
            requesterUserId = "user-org-ldm",
        )
        assertTrue(deleteSuccess.isSuccess)

        // 5. Organizador tentando adicionar participante via LocalDataManager -> Deve falhar
        val newGuest = Participant(
            id = "part-guest-ldm",
            name = "Novo Hóspede",
            role = "Participante",
            isOrganizer = false,
        )
        val addFail = LocalDataManager.addParticipantToTrip(
            tripId = tripId,
            participant = newGuest,
            requesterUserId = "user-org-ldm",
        )
        assertTrue(addFail.isFailure)
        assertEquals("Apenas o criador da viagem pode adicionar participantes.", addFail.exceptionOrNull()?.message)

        // 6. Criador adicionando participante via LocalDataManager -> Deve ter sucesso
        val addSuccess = LocalDataManager.addParticipantToTrip(
            tripId = tripId,
            participant = newGuest,
            requesterUserId = "user-creator-ldm",
        )
        assertTrue(addSuccess.isSuccess)
    }

    @Test
    fun testOnlyCreatorCanEditTrip() {
        val tripId = "trip-edit-test-${kotlin.random.Random.nextInt(1000, 9999)}"
        val creator = Participant(
            id = "creator-part",
            name = "Criador",
            userId = "user-creator-edit",
            role = "Organizador",
            isOrganizer = true,
            isCreator = true,
        )
        val organizer = Participant(
            id = "org-part",
            name = "Organizador Secundário",
            userId = "user-org-edit",
            role = "Organizador",
            isOrganizer = true,
            isCreator = false,
        )
        val participant = Participant(
            id = "regular-part",
            name = "Viajante",
            userId = "user-part-edit",
            role = "Participante",
            isOrganizer = false,
            isCreator = false,
        )
        val trip = Trip(
            id = tripId,
            userId = "user-creator-edit",
            title = "Viagem Original",
            destination = "Destino Original",
            status = TripStatus.UPCOMING,
            totalAmount = 1000.0,
            myBalance = 0.0,
            startDate = "2026-12-01",
            endDate = "2026-12-05",
            participants = listOf(creator, organizer, participant),
        )
        LocalDataManager.addTrip(trip)

        val modifiedTrip = trip.copy(title = "Viagem Alterada por Terceiros")

        // 1. Participante comum tentando editar a viagem -> Deve falhar
        val partEditFail = LocalDataManager.updateTrip(
            updated = modifiedTrip,
            requesterUserId = "user-part-edit",
        )
        assertTrue(partEditFail.isFailure, "Participante não pode editar a viagem.")
        assertEquals("Apenas o criador pode editar a viagem que criou.", partEditFail.exceptionOrNull()?.message)

        // 2. Organizador (não criador) tentando editar a viagem -> Deve falhar
        val orgEditFail = LocalDataManager.updateTrip(
            updated = modifiedTrip,
            requesterUserId = "user-org-edit",
        )
        assertTrue(orgEditFail.isFailure, "Apenas o criador pode editar a viagem que criou.")
        assertEquals("Apenas o criador pode editar a viagem que criou.", orgEditFail.exceptionOrNull()?.message)

        // 3. Criador da viagem editando a viagem -> Deve ter sucesso
        val creatorEditSuccess = LocalDataManager.updateTrip(
            updated = modifiedTrip,
            requesterUserId = "user-creator-edit",
        )
        assertTrue(creatorEditSuccess.isSuccess, "O criador tem permissão para editar a viagem que criou.")
        assertEquals("Viagem Alterada por Terceiros", LocalDataManager.getTrip(tripId)?.title)
    }
}
