package com.imd.tripsplit.data.storage

import android.content.Context
import java.io.File

object AndroidStorageContext {
    var context: Context? = null
}

actual class PlatformStorage actual constructor() {
    private val dir: File by lazy {
        val ctx = AndroidStorageContext.context
        val baseDir = ctx?.filesDir ?: File(System.getProperty("java.io.tmpdir") ?: ".", "tripsplit")
        if (!baseDir.exists()) {
            baseDir.mkdirs()
        }
        baseDir
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
