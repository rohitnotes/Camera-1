package xyz.romakononovich.camera.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
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

inline fun ifElseLollipop(action1: () -> Unit, action2: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        action1()
    } else {
        action2()
    }
}

fun Context.toast(message: CharSequence): Toast = Toast
        .makeText(this, message, Toast.LENGTH_SHORT)
        .apply {
            show()
        }
