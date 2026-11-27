package com.imd.tripsplit.data.storage

import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSUserDomainMask
import platform.Foundation.stringWithContentsOfFile
import platform.Foundation.writeToFile

actual class PlatformStorage actual constructor() {
    private val documentsDirectory: String by lazy {
        val paths = NSFileManager.defaultManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
        val documentsURL = paths.firstOrNull() as? platform.Foundation.NSURL
        documentsURL?.path ?: ""
    }

    actual fun readString(fileName: String): String? {
        val path = if (documentsDirectory.isNotEmpty()) "$documentsDirectory/$fileName" else fileName
        return try {
            NSString.stringWithContentsOfFile(path, NSUTF8StringEncoding, null) as? String
        } catch (e: Exception) {
            null
        }
    }

    @Suppress("CAST_NEVER_SUCCEEDS")
    actual fun writeString(
        fileName: String,
        content: String,
    ) {
        val path = if (documentsDirectory.isNotEmpty()) "$documentsDirectory/$fileName" else fileName
        try {
            (content as NSString).writeToFile(path, true, NSUTF8StringEncoding, null)
        } catch (e: Exception) {
            // Ignored
        }
    }
}
