package xyz.romakononovich.camera.presentation.main

import xyz.romakononovich.camera.data.model.FlashMode
import xyz.romakononovich.camera.domain.api.CameraApi
import xyz.romakononovich.camera.presentation.base.BasePresenterImpl
import xyz.romakononovich.camera.presentation.router.Router
import javax.inject.Inject

/**
 * Created by RomanK on 05.05.18.
 */
class MainPresenter<V : MainContract.View>
@Inject constructor(
        private val cameraApi: CameraApi,
        private val router: Router
) : BasePresenterImpl<V>(), MainContract.Presenter<V> {

    init {
        cameraApi.run {
            onCameraChanged = {
                view()?.onCameraChanged?.invoke(it)
            }
            onCamerasRetrieved = {
                if (it == 0)
                    view()?.hideChangeCamera()
            }
            onFlashModeChanged = {
                when (it) {
                    FlashMode.NONE -> view()?.hideFlash()
                    FlashMode.ON -> view()?.showFlashOn()
                    FlashMode.AUTO -> view()?.showFlashAuto()
                    FlashMode.OFF -> view()?.showFlashOff()
                }
            }
            onPhotoSaved = {
                view()?.showPhotoSavedToast(it)
                view()?.unlockMakePhoto()
                view()?.setPreviewLastPhoto(it)
            }
            onPhotoSavedFail = {
            }
        }
    }

    override fun start() {
        cameraApi.start()
    }

    override fun stop() {
        cameraApi.stop()
    }

    override fun changeCamera() {
        cameraApi.changeCamera()
    }

    override fun makePhoto() {
        cameraApi.makePhoto()
    }

    override fun changeFlash() {
        cameraApi.changeFlashMode()
    }

    override fun openGallery() {
        router.openGallery()
    }

}