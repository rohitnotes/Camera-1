package xyz.romakononovich.camera.presentation.gallery

import xyz.romakononovich.camera.domain.api.BarcodeDetectorApi
import xyz.romakononovich.camera.domain.api.PhotoRepository
import xyz.romakononovich.camera.presentation.router.Router

/**
 * Created by RomanK on 09.05.18.
 */
class GalleryPresenter(private val v: GalleryContract.View,
                       private val codeApi: BarcodeDetectorApi,
                       private val repository: PhotoRepository,
                       private val router: Router) : GalleryContract.Presenter {


    override fun getPhoto() {


    }


    init {
        v.presenter = this
        codeApi.run {
            onBarcodeDetect = {
                v.onBarcodeDetect.invoke(it)
            }
        }

    }

    override fun startBarcodeDetector(id: Int) {
        codeApi.start(repository.getListPhoto()[id])
    }

    override fun deletePhoto(id: Int) {
        repository.deletePhoto(repository.getListPhoto()[id])
    }

    override fun sharePhoto(id: Int) {
        router.sharePhoto(repository.getListPhoto()[id])
    }

    override fun start() {
        v.initViewPager(repository.getListPhoto())
    }

    override fun stop() {

    }
}