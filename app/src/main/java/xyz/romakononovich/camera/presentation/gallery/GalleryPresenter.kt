package xyz.romakononovich.camera.presentation.gallery

import xyz.romakononovich.camera.domain.api.BarcodeDetectorApi
import xyz.romakononovich.camera.domain.api.PhotoApi
import xyz.romakononovich.camera.presentation.base.BasePresenterImpl
import xyz.romakononovich.camera.presentation.router.Router
import javax.inject.Inject

/**
 * Created by RomanK on 09.05.18.
 */
class GalleryPresenter<V : GalleryContract.View>
@Inject constructor(private val barcodeApi: BarcodeDetectorApi,
                    private val photoApi: PhotoApi,
                    private val router: Router) : BasePresenterImpl<V>(), GalleryContract.Presenter<V> {


    init {
        barcodeApi.run {
            onBarcodeDetect = {
                view()?.onBarcodeDetect?.invoke(it)
            }
        }
        photoApi.run {
            onGetInfo = {
                view()?.onGetInfo?.invoke(it)
            }
        }
    }

    override fun startBarcodeDetector(id: Int) {
        barcodeApi.start(photoApi.getListPhoto()[id])
    }

    override fun openFaceDetectActivity(id: Int) {
        router.openFaceDetectActivity(photoApi.getListPhoto()[id])
    }

    override fun showInfoPhoto(id: Int) {
        photoApi.getExif(photoApi.getListPhoto()[id])
    }

    override fun deletePhoto(id: Int) {
        photoApi.deletePhoto(photoApi.getListPhoto()[id])
    }

    override fun sharePhoto(id: Int) {
        router.sharePhoto(photoApi.getListPhoto()[id])
    }

    override fun printPhoto(id: Int) {
        view()?.printPhoto(photoApi.getListPhoto()[id])
    }

    override fun start() {
        barcodeApi.initializeBarcodeDetector()
        view()?.initViewPager(photoApi.getListPhoto())
    }

    override fun refreshList() {
        view()?.refreshListPager(photoApi.getListPhoto())
    }

    override fun stop() {

    }
}