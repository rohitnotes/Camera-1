package xyz.romakononovich.camera.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import java.io.File

/**
 * Created by RomanK on 05.05.18.
 */

inline fun Any.catchAll(message: String, action: () -> Unit) {
    try {
        action()
    } catch (t: Throwable) {
        Log.e(this::class.java.simpleName, "Failed to $message. ${t.message}", t)
    }
}

fun Context.toast(message: CharSequence): Toast = Toast
        .makeText(this, message, Toast.LENGTH_SHORT)
        .apply {
            show()
        }

fun getStorage(context: Context) =  File(context.filesDir, ALBUM_NAME)