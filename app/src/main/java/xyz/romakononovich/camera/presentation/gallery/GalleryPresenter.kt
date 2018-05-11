package xyz.romakononovich.camera.presentation.gallery

import xyz.romakononovich.camera.domain.api.BarcodeDetectorApi
import xyz.romakononovich.camera.domain.api.PhotoRepository
import xyz.romakononovich.camera.presentation.base.BasePresenterImpl
import xyz.romakononovich.camera.presentation.router.RouterGallery
import javax.inject.Inject

/**
 * Created by RomanK on 09.05.18.
 */
class GalleryPresenter<V: GalleryContract.View>
@Inject constructor(private val barcodeApi: BarcodeDetectorApi,
                    private val repository: PhotoRepository,
                    private val router: RouterGallery) : BasePresenterImpl<V>(), GalleryContract.Presenter<V> {


    override fun getPhoto() {


    }


    init {
        barcodeApi.run {
            onBarcodeDetect = {
                view()?.onBarcodeDetect?.invoke(it)
            }
        }
    }

    override fun startBarcodeDetector(id: Int) {
        barcodeApi.start(repository.getListPhoto()[id])
    }

    override fun deletePhoto(id: Int) {
        repository.deletePhoto(repository.getListPhoto()[id])
    }

    override fun sharePhoto(id: Int) {
        router.sharePhoto(repository.getListPhoto()[id])
    }

    override fun start() {
        view()?.initViewPager(repository.getListPhoto())
    }

    override fun stop() {

    }
}