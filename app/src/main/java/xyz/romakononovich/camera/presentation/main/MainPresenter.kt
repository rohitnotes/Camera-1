package xyz.romakononovich.camera.presentation.main

import xyz.romakononovich.camera.data.model.FlashMode
import xyz.romakononovich.camera.domain.api.CameraApi
import xyz.romakononovich.camera.presentation.router.Router

/**
 * Created by RomanK on 05.05.18.
 */
class MainPresenter(
        private val v: MainContract.View,
        private val cameraApi: CameraApi,
        private val router: Router
) : MainContract.Presenter {

    init {
        v.presenter = this
        cameraApi.run {
            onCameraChanged = {
                v.onCameraChanged.invoke(it)
            }
            onCamerasRetrieved = {
                if (it == 0)
                    v.hideChangeCamera()
            }
            onFlashModeChanged = {
                when (it) {
                    FlashMode.NONE -> v.hideFlash()
                    FlashMode.ON -> v.showFlashOn()
                    FlashMode.AUTO -> v.showFlashAuto()
                    FlashMode.OFF -> v.showFlashOff()
                }
            }
            onPhotoSaved = {
                v.showPhotoSavedToast(it)
                v.unlockMakePhoto()
                v.setPreviewLastPhoto(it)
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