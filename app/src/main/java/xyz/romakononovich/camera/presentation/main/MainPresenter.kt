package xyz.romakononovich.camera.presentation.main

import android.Manifest
import android.content.pm.PackageManager
import xyz.romakononovich.camera.data.model.FlashMode
import xyz.romakononovich.camera.domain.api.CameraApi

/**
 * Created by RomanK on 05.05.18.
 */
class MainPresenter(
        private val v: MainContract.View,
        private val cameraApi: CameraApi
) : MainContract.Presenter {
    private companion object {
        const val PERMISSION_CAMERA = Manifest.permission.CAMERA
        const val REQUEST_PERMISSION_CAMERA = 0
        const val PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
        const val REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 1
    }

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
        if (v.isPermissionGranted(PERMISSION_CAMERA)) {
            cameraApi.start()
        } else {
            v.requestPermission(PERMISSION_CAMERA, REQUEST_PERMISSION_CAMERA)
        }
    }

    // https://developer.android.com/training/permissions/requesting.html
    override fun permissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION_CAMERA -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    cameraApi.start()
                } else {
                    v.showCannotShowCameraToast()
                }
                return
            }
            REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    cameraApi.makePhoto()
                } else {
                    v.showCannotSavePhotoToast()
                }
                return
            }
        }
    }

    override fun stop() {
        cameraApi.stop()
    }

    override fun changeCamera() {
        cameraApi.changeCamera()
    }

    override fun makePhoto() {
        if (v.isPermissionGranted(PERMISSION_WRITE_EXTERNAL_STORAGE)) {
            v.lockMakePhoto()
            cameraApi.makePhoto()
        } else {
            v.requestPermission(PERMISSION_WRITE_EXTERNAL_STORAGE, REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE)
        }

    }

    override fun changeFlash() {
        cameraApi.changeFlashMode()
    }


}