package com.imd.tripsplit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.imd.tripsplit.model.TripFilter
import com.imd.tripsplit.ui.components.TripDetailsScreen
import com.imd.tripsplit.ui.screens.home.HomeScreen
import com.imd.tripsplit.ui.screens.trip.CreateTripScreen

@Composable
fun TripSplitNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Route.Home,
        modifier = modifier,
    ) {
        composable<Route.Home> {
            HomeScreen(
                trips = emptyList(),
                selectedFilter = TripFilter.ALL,
                onFilterSelected = {},
                onNavigateToTripEdit = { tripId ->
                    navController.navigate(Route.TripDetail(tripId = tripId))
                },
                onCreateTripClick = {
                    navController.navigate(Route.NewTrip)
                }
            )
        }

        composable<Route.TripDetail> { entry ->
            val args = entry.toRoute<Route.TripDetail>()
            TripDetailsScreen(
                tripId = args.tripId,
                onBack = { navController.popBackStack() },
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
