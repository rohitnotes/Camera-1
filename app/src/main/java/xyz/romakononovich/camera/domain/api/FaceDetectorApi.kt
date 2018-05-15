package xyz.romakononovich.camera.domain.api

import android.graphics.Bitmap

/**
 * Created by RomanK on 12.05.18.
 */
interface FaceDetectorApi {

    var onFaceDetect: (bitmap: Bitmap) -> Unit

    var onFaceDetectError: (source: String) -> Unit

    var onErrorNoFace: (source: String) -> Unit

    fun start(path: String)

    fun stop()

}