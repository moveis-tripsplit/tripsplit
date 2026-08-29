package com.imd.tripsplit

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform