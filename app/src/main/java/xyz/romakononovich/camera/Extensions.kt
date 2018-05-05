package xyz.romakononovich.camera

import android.content.Context
import android.util.Log
import android.widget.Toast

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