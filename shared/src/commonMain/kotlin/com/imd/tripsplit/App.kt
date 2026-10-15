@file:Suppress("FunctionName")

package com.imd.tripsplit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.imd.tripsplit.navigation.TripSplitNavHost
import com.imd.tripsplit.ui.theme.TripSplitTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
@Preview
fun App() {
    TripSplitTheme {
        TripSplitNavHost()
    }
}