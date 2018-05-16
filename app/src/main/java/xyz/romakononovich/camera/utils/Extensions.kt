package xyz.romakononovich.camera.utils

import android.content.Context
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.animation.RotateAnimation
import android.widget.Toast
import java.io.File
import java.util.ArrayList

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

fun View.startRotate(rotateAnimation: RotateAnimation) {
    if (this.visibility == View.VISIBLE) {
        this.startAnimation(rotateAnimation)
    }


}

fun getSortedByNameListFiles(): MutableList<String> {
    val pathsList: MutableList<String> = ArrayList()

    return if (getListFiles() != null) {

        for (image in getListFiles().sortedWith(Comparator<File> { p0, p1 -> p0.name.compareTo(p1.name) }).reversed()) {
            pathsList.add(image.absolutePath)
        }
        pathsList
    } else {
        pathsList
    }
}

fun getListFiles() = File(Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_PICTURES), ALBUM_NAME).listFiles()


fun RotateOrientationEventListener.checkDetectOrientation() {
    if (this.canDetectOrientation()) {
        this.enable()
    } else {
        this.disable()
    }
}
