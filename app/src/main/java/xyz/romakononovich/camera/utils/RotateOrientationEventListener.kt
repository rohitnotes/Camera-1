package xyz.romakononovich.camera.utils

import android.content.Context

/**
 * Created by RomanK on 11.05.18.
 */
abstract class RotateOrientationEventListener : SimpleOrientationEventListener {
    constructor(context: Context) : super(context)
    constructor(context: Context, rate: Int) : super(context, rate)

    override fun onChanged(lastOrientation: Int, orientation: Int) {
        val startDeg = when (lastOrientation){
            0 -> if (orientation == 3) 360 else 0
            1 -> 90
            2 -> 180
            else -> 270
        }

        val endDeg = when (orientation) {
            0 -> if (lastOrientation == 1) 0 else 360
            1 -> 90
            2 -> 180
            else -> 270
        }
        onRotateChanged(startDeg, endDeg)
    }

    abstract fun onRotateChanged(startDeg: Int, endDeg: Int)
}