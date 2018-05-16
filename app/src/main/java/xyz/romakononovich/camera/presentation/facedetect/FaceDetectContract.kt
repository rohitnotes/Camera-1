package xyz.romakononovich.camera.presentation.facedetect

import android.graphics.Bitmap
import xyz.romakononovich.camera.presentation.base.BasePresenter
import xyz.romakononovich.camera.presentation.base.BaseView

/**
 * Created by RomanK on 12.05.18.
 */
interface FaceDetectContract {
    interface View : BaseView {
        var onFaceDetect: () -> Unit

        var onFaceShow: (bitmap: Bitmap) -> Unit

        var onShowLandmarks: (bitmap: Bitmap) -> Unit

        var onFaceDetectError: (source: String) -> Unit

        var onErrorNoFace: (source: String) -> Unit

        fun showPhotoSavedToast(path: String)

        fun showIconEye()

        fun closeActivity()
    }

    interface Presenter<V : FaceDetectContract.View> : BasePresenter<V> {
        fun startFaceDetector(path: String)

        fun showLandmarks()

        fun savePhoto()

        fun start()

        fun stop()
    }
}