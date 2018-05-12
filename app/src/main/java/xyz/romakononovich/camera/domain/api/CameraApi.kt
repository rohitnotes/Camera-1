package xyz.romakononovich.camera.domain.api

import android.hardware.Camera
import xyz.romakononovich.camera.data.model.FlashMode

/**
 * Created by RomanK on 05.05.18.
 */
interface CameraApi {

    var onCameraChanged: (camera: Camera) -> Unit

    var onCamerasRetrieved: (count: Int) -> Unit

    var onFlashModeChanged: (FlashMode) -> Unit

    var onPhotoSaved: (path: String)  -> Unit

    var onPhotoSavedFail: () -> Unit

    fun start()

    fun stop()

    fun changeCamera()

    fun makePhoto(orientationDegrees: Int)

    fun changeFlashMode()

}