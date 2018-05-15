package xyz.romakononovich.camera.presentation.facedetect

import android.graphics.Bitmap
import xyz.romakononovich.camera.presentation.base.BasePresenter
import xyz.romakononovich.camera.presentation.base.BaseView

/**
 * Created by RomanK on 12.05.18.
 */
interface FaceDetectContract {
    interface View : BaseView {
        var onFaceDetect: (bitmap: Bitmap) -> Unit

        var onFaceDetectError: (source: String) -> Unit

        var onErrorNoFace: (source: String) -> Unit

        fun showDetectFace()
    }

    interface Presenter<V : FaceDetectContract.View> : BasePresenter<V> {
        fun startFaceDetector(path: String)

        fun start()

        fun stop()
    }
}