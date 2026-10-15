@file:Suppress("FunctionName")

package com.imd.tripsplit

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.imd.tripsplit.navigation.TripSplitNavHost
import com.imd.tripsplit.ui.theme.TripSplitTheme

@Composable
@Preview
fun App() {
    TripSplitTheme {
        TripSplitNavHost()
    }
}
