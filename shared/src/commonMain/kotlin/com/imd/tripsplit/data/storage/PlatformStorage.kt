package com.imd.tripsplit.data.storage

expect class PlatformStorage() {
    fun readString(fileName: String): String?

    fun writeString(
        fileName: String,
        content: String
    )
}
