package com.imd.tripsplit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.imd.tripsplit.data.TripRepository
import com.imd.tripsplit.model.TripFilter
import com.imd.tripsplit.ui.screens.home.HomeScreen
import com.imd.tripsplit.ui.screens.login.LoginScreen
import com.imd.tripsplit.ui.screens.login.RegisterScreen
import com.imd.tripsplit.ui.screens.trip.CreateTripScreen

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

        composable<Route.Register> {
            RegisterScreen(
                onRegisterClick = {
                    // Lógica do botão Registrar
                },
                onLoginClick = {
                    navController.popBackStack()
                },
            )
        }

        composable<Route.Home> {
            val trips by TripRepository.trips.collectAsState()
            var selectedFilter by remember { mutableStateOf(TripFilter.ALL) }

            HomeScreen(
                trips = trips,
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it },
                onNavigateToTripEdit = { tripId ->
                    navController.navigate(Route.TripDetail(tripId = tripId))
                },
                onCreateTripClick = {
                    navController.navigate(Route.NewTrip)
                },
            )
        }

        composable<Route.TripDetail> { entry ->
            val args = entry.toRoute<Route.TripDetail>()
            CreateTripScreen(
                tripId = args.tripId,
                onBackClick = { navController.popBackStack() },
                onTripCreatedSuccess = { navController.popBackStack() },
            )
        }

        composable<Route.NewTrip> {
            CreateTripScreen(
                onBackClick = { navController.popBackStack() },
                onTripCreatedSuccess = {
                    navController.popBackStack()
                }
            )
        }
    }
}
