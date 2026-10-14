package com.imd.tripsplit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.imd.tripsplit.ui.home.HomeRoute
import com.imd.tripsplit.ui.screens.login.LoginScreen
import com.imd.tripsplit.ui.trip.TripDetailScreen

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
        composable<Route.Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Route.Home) {
                        popUpTo<Route.Login> { inclusive = true }
                    }
                },
                onForgotPasswordClick = {
                    // TODO: navegar para recuperação de senha
                },
                onSignUpClick = {
                    // TODO: navegar para cadastro
                },
            )
        }

        composable<Route.Home> {
            HomeRoute(
                onTripClick = { id -> navController.navigate(Route.TripDetail(tripId = id)) },
                onNewTripClick = { navController.navigate(Route.NewTrip) },
            )
        }

        composable<Route.TripDetail>(
            deepLinks = listOf(navDeepLink<Route.TripDetail>(basePath = TRIP_DEEP_LINK_BASE)),
        ) { entry ->
            val args = entry.toRoute<Route.TripDetail>()
            TripDetailScreen(
                tripId = args.tripId,
                onBack = { navController.popBackStack() },
            )
        }

        composable<Route.NewTrip> {
            TripDetailScreen(tripId = "nova", onBack = { navController.popBackStack() })
        }
    }
}