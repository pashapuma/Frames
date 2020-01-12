package dev.jahir.frames.extensions

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import dev.jahir.frames.R
import java.io.File

fun File.getUri(context: Context?): Uri? {
    context ?: return null
    return try {
        FileProvider.getUriForFile(context, context.packageName + ".fileProvider", this)
    } catch (e: Exception) {
        try {
            Uri.fromFile(this)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

@Suppress("DEPRECATION")
fun Context.getExternalStorageFolder(): File? {
    val externalStorage = try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        } else {
            Environment.getExternalStorageDirectory()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
    val appStorage = try {
        getExternalFilesDir(null)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
    return if (appStorage?.absolutePath.orEmpty().contains(packageName)) externalStorage
    else appStorage
}

fun File.createIfDidNotExist(): Boolean = try {
    if (!exists()) mkdirs() else true
} catch (e: Exception) {
    false
}

fun Context.getDefaultWallpapersDownloadFolder(): File? {
    val externalFolder = getExternalStorageFolder()
    val folder = File("$externalFolder${File.separator}${getString(R.string.app_name)}")
    folder.createIfDidNotExist()
    return folder
}