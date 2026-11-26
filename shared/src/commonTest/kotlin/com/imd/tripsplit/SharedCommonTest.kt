package com.imd.tripsplit

import com.imd.tripsplit.data.TripRepository
import com.imd.tripsplit.data.UserRepository
import com.imd.tripsplit.model.Trip
import com.imd.tripsplit.model.TripStatus
import com.imd.tripsplit.ui.screens.login.cadastro.RegisterUiState
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SharedCommonTest {
    @Test
    fun testUserRegistrationAndLogin() {
        val email = "teste_${kotlin.random.Random.nextInt(10000, 99999)}@tripsplit.com"
        val password = "senhaSecreta123"

        // 1. Cadastrar usuário
        val registerResult =
            UserRepository.register(
                name = "Viajante Teste",
                email = email,
                password = password,
                birthDate = "2000-01-01",
            )
        assertTrue(registerResult.isSuccess, "O cadastro deve ter sucesso.")
        val registeredUser = registerResult.getOrNull()
        assertNotNull(registeredUser)
        assertEquals("Viajante Teste", registeredUser.name)

        // 2. Não permitir duplicidade de email
        val duplicateResult =
            UserRepository.register(
                name = "Outro Nome",
                email = email,
                password = "outrasenha",
                birthDate = null,
            )
        assertTrue(duplicateResult.isFailure, "Não deve permitir e-mails duplicados.")

        // 3. Fazer logout
        UserRepository.logout()
        assertNull(UserRepository.currentUser.value)

        // 4. Tentar login com senha errada
        val wrongPasswordResult = UserRepository.login(email, "senhaErrada")
        assertTrue(wrongPasswordResult.isFailure, "Senha incorreta deve falhar.")

        // 5. Fazer login com credenciais corretas
        val loginResult = UserRepository.login(email, password)
        assertTrue(loginResult.isSuccess, "Login com dados corretos deve ter sucesso.")
        assertEquals(registeredUser.id, UserRepository.currentUser.value?.id)

        // 6. Cadastrar viagem associada ao usuário
        val tripId = "trip-${kotlin.random.Random.nextInt(1000, 9999)}"
        val trip =
            Trip(
                id = tripId,
                userId = registeredUser.id,
                title = "Viagem para Fernando de Noronha",
                destination = "Fernando de Noronha",
                status = TripStatus.UPCOMING,
                totalAmount = 2500.0,
                myBalance = 0.0,
                startDate = "2026-12-01",
                endDate = "2026-12-07",
            )
        TripRepository.addTrip(trip)

        val retrievedTrip = TripRepository.getTrip(tripId)
        assertNotNull(retrievedTrip)
        assertEquals("Viagem para Fernando de Noronha", retrievedTrip.title)
        assertEquals(registeredUser.id, retrievedTrip.userId)

        // 7. Viagem aparece na lista do usuário
        val userTrips = TripRepository.getTripsForCurrentUser()
        assertTrue(userTrips.any { it.id == tripId }, "A viagem cadastrada deve estar na lista do usuário.")
    }

    @Test
    fun testRegisterUiStateValidation() {
        var state = RegisterUiState()
        assertFalse(state.isRegisterEnabled, "Estado inicial não deve permitir registro")
        assertTrue(state.isBirthDateEmpty, "Data de nascimento deve iniciar vazia")

        state = state.copy(
            nome = "João da Silva",
            email = "joao@exemplo.com",
            password = "123",
            confirmPassword = "123"
        )
        assertFalse(state.isRegisterEnabled, "Sem data de nascimento, registro deve estar desabilitado")

        state = state.copy(
            birthDate = LocalDate(2000, 5, 20),
            birthDateInput = "20/05/2000"
        )
        assertTrue(state.isRegisterEnabled, "Com todos os dados válidos, registro deve estar habilitado")
        assertFalse(state.isBirthDateEmpty, "Data de nascimento não deve estar vazia")
    }
}
