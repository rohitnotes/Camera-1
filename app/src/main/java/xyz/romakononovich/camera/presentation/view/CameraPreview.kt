package xyz.romakononovich.camera.presentation.view

import android.content.Context
import android.hardware.Camera
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.io.IOException

/**
 * Created by RomanK on 05.05.18.
 */
/** A basic Camera preview class  */
class CameraPreview(context: Context, private val camera: Camera?) : SurfaceView(context), SurfaceHolder.Callback {

    private companion object {
        val TAG = CameraPreview::class.java.simpleName
    }
    private val surfaceHolder: SurfaceHolder = holder

    init {
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        surfaceHolder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            camera?.setPreviewDisplay(holder)
            camera?.startPreview()
        } catch (e: IOException) {
            Log.d(TAG, "Error setting camera preview: ${e.message}")
        }

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, w: Int, h: Int) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (surfaceHolder.surface == null) {
            // preview surface does not exist
            return
        }

        // stop preview before making changes
        try {
            camera?.stopPreview()
        } catch (e: Exception) {
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            camera?.setPreviewDisplay(surfaceHolder)
            camera?.startPreview()

        } catch (e: Exception) {
            Log.d(TAG, "Error starting camera preview: ${e.message}")
        }
    }

}