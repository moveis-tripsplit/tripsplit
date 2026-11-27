package com.imd.tripsplit.data.remote

import io.ktor.client.HttpClient

expect fun createPlatformHttpClient(): HttpClient
