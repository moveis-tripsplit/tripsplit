
package com.imd.tripsplit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.imd.tripsplit.data.DespesasRepositoryImpl
import com.imd.tripsplit.data.TripRepository
import com.imd.tripsplit.model.TripFilter
import com.imd.tripsplit.presentation.trip.expenses.TelaDeDespesasScreen
import com.imd.tripsplit.ui.screens.home.HomeScreen
import com.imd.tripsplit.ui.screens.home.perfil.PerfilScreen
import com.imd.tripsplit.ui.screens.home.trip.CreateTripScreen
import com.imd.tripsplit.ui.screens.login.LoginScreen
import com.imd.tripsplit.ui.screens.login.cadastro.RegisterScreen
import kotlinx.coroutines.launch

@Composable
fun TripSplitNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Route.Login,
        modifier = modifier,
    ) {
        // LOGIN
        composable<Route.Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Route.Home) {
                        popUpTo<Route.Login> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onRegisterClick = {
                    navController.navigate(Route.Register)
                },
            )
        }

        // PERFIL
        composable<Route.Perfil> {
            PerfilScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onLogoutClick = {
                    com.imd.tripsplit.data.UserRepository.logout()
                    navController.navigate(Route.Login) {
                        popUpTo<Route.Home> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
            )
        }

        // CADASTRO
        composable<Route.Register> {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Route.Home) {
                        popUpTo<Route.Login> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onLoginClick = {
                    navController.popBackStack()
                },
            )
        }

        // HOME
        composable<Route.Home> {
            val trips by TripRepository.trips.collectAsState()

            var selectedFilter by remember {
                mutableStateOf(TripFilter.ALL)
            }
            val allTrips by TripRepository.trips.collectAsState()
            val currentUser by com.imd.tripsplit.data.UserRepository.currentUser.collectAsState()
            val userTrips =
                remember(allTrips, currentUser) {
                    val user = currentUser
                    if (user != null) {
                        val uid = user.id
                        val uEmail = user.email.trim()
                        allTrips.filter { trip ->
                            trip.userId == uid ||
                                trip.userId.isBlank() ||
                                trip.participants.any { p ->
                                    (p.userId != null && p.userId == uid) ||
                                        (!p.email.isNullOrBlank() && p.email.trim().equals(uEmail, ignoreCase = true))
                                }
                        }
                    } else {
                        allTrips
                    }
                }
            var selectedFilter by remember { mutableStateOf(TripFilter.ALL) }

            HomeScreen(
                trips = userTrips,
                selectedFilter = selectedFilter,
                onFilterSelected = {
                    selectedFilter = it
                },
                onNavigateToTripEdit = { tripId ->
                    navController.navigate(
                        Route.TripDetail(tripId = tripId)
                    )
                },
                onCreateTripClick = {
                    navController.navigate(Route.NewTrip)
                },
                onPerfilClick = {
                    navController.navigate(Route.Perfil)
                },
            )
        }

        // DETALHES / EDIÇÃO DA VIAGEM
        composable<Route.TripDetail>(
            deepLinks =
                listOf(
                    navDeepLink<Route.TripDetail>(
                        basePath = "tripsplit://trip",
                    )
                )
        ) { entry ->

            val args = entry.toRoute<Route.TripDetail>()

            CreateTripScreen(
                tripId = args.tripId,
                onBackClick = {
                    navController.popBackStack()
                },
                onTripCreatedSuccess = {
                    navController.popBackStack()
                },
                onManageExpensesClick = { tripId ->
                    navController.navigate(
                        Route.TripExpenses(tripId = tripId)
                    )
                },
            )
        }

        // NOVA VIAGEM
        composable<Route.NewTrip> {
            CreateTripScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onTripCreatedSuccess = {
                    navController.popBackStack()
                }
            )
        }

        // DESPESAS DA VIAGEM
        composable<Route.TripExpenses> { entry ->

            val args = entry.toRoute<Route.TripExpenses>()

            val trip =
                remember(args.tripId) {
                    TripRepository.getTrip(args.tripId)
                }

            // Observa as despesas cadastradas para esta viagem
            val despesas by DespesasRepositoryImpl
                .observarDespesas(args.tripId)
                .collectAsState(initial = emptyList())

            val scope = rememberCoroutineScope()

            TelaDeDespesasScreen(
                nomeViagem = trip?.title ?: "Viagem",
                despesas = despesas,
                participantes = trip?.participants ?: emptyList(),
                totalViagem = despesas.sumOf { it.valorTotal },
                meuSaldo = 0.0,
                tripId = args.tripId,
                onVoltar = {
                    navController.popBackStack()
                },
                onAdicionarDespesa = {
                    // O modal já é controlado pela própria tela.
                },
                onSalvarDespesa = { despesa ->
                    scope.launch {
                        DespesasRepositoryImpl.adicionar(despesa)
                    }
                },
                onEditarDespesa = {
                    // A edição será conectada posteriormente.
                },
                onExcluirDespesa = { despesa ->
                    scope.launch {
                        DespesasRepositoryImpl.excluir(despesa.id)
                    }
                }
            )
        }
    }
}
