package xyz.romakononovich.camera.presentation.gallery

import android.graphics.Bitmap
import xyz.romakononovich.camera.presentation.base.BasePresenter
import xyz.romakononovich.camera.presentation.base.BaseView

/**
 * Created by RomanK on 09.05.18.
 */
interface GalleryContract {
    interface View : BaseView<GalleryContract.Presenter> {
        var onFaceDetect: (bitmap: Bitmap) -> Unit
        var onBarcodeDetect: (source: String) -> Unit
        fun showDetectFace()
    }

    interface Presenter : BasePresenter {
        fun startBarcodeDetector(path: String)
    }
}