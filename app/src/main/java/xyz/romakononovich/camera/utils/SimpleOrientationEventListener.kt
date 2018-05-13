package xyz.romakononovich.camera.utils

import android.content.Context
import android.view.OrientationEventListener

/**
 * Created by RomanK on 11.05.18.
 */
abstract class SimpleOrientationEventListener(context: Context, rate: Int) : OrientationEventListener(context, rate) {

    private var lastOrientation = 0

    private companion object {
        const val ORIENTATION_PORTRAIT = 0
        const val ORIENTATION_LANDSCAPE = 1
        const val ORIENTATION_PORTRAIT_REVERSE = 2
        const val ORIENTATION_LANDSCAPE_REVERSE = 3
    }

    override fun onOrientationChanged(orientation: Int) {
        if (orientation < 0) return

        val currentOrientation = when (true) {
            orientation in 45..135 -> ORIENTATION_LANDSCAPE_REVERSE
            orientation in 135..225 -> ORIENTATION_PORTRAIT_REVERSE
            orientation in 225..315 -> ORIENTATION_LANDSCAPE
            else -> ORIENTATION_PORTRAIT
        }
        if (currentOrientation != lastOrientation) {
            onChanged(lastOrientation, currentOrientation)
            lastOrientation = currentOrientation
        }
    }

    abstract fun onChanged(lastOrientation: Int, orientation: Int)
}
