package xyz.romakononovich.camera.presentation.gallery

import xyz.romakononovich.camera.domain.api.BarcodeDetectorApi
import xyz.romakononovich.camera.domain.api.FaceDetectorApi

/**
 * Created by RomanK on 09.05.18.
 */
class GalleryPresenter(private val v: GalleryContract.View,
                       private val codeApi: BarcodeDetectorApi) : GalleryContract.Presenter {
    init {
        v.presenter = this
        codeApi.run {
            onBarcodeDetect = {
                v.onBarcodeDetect.invoke(it)
            }
        }

    }

    override fun startBarcodeDetector(path: String) {
        codeApi.start(path)
    }

    override fun start() {

    }

    override fun stop() {

    }
}