package com.imd.tripsplit.data.storage

import java.io.File

actual class PlatformStorage actual constructor() {
    private val dir: File by lazy {
        val userHome = System.getProperty("user.home") ?: "."
        val folder = File(userHome, ".tripsplit")
        if (!folder.exists()) {
            folder.mkdirs()
        }
        folder
    }

    actual fun readString(fileName: String): String? {
        val file = File(dir, fileName)
        return if (file.exists()) {
            try {
                file.readText()
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    actual fun writeString(fileName: String, content: String) {
        try {
            val file = File(dir, fileName)
            file.writeText(content)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
