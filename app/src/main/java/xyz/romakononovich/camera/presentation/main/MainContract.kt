package xyz.romakononovich.camera.presentation.main

import android.hardware.Camera
import xyz.romakononovich.camera.presentation.base.BasePresenter
import xyz.romakononovich.camera.presentation.base.BaseView

/**
 * Created by RomanK on 05.05.18.
 */
interface MainContract {

    interface View : BaseView {

        var onCameraChanged: (camera: Camera) -> Unit

        fun makePhoto()

        fun openGallery()

        fun hideChangeCamera()

        fun hideFlash()

        fun showFlashOn()

        fun showFlashAuto()

        fun showFlashOff()

        fun showPhotoSavedToast(path: String)

        fun lockMakePhoto()

        fun unlockMakePhoto()

        fun getPathLastPhoto(): String?

        fun setPreviewLastPhoto(path: String?)

        fun showCannotShowCameraToast()

        fun showCannotSavePhotoToast()

        fun showCannotOpenGalleryToast()

        fun requestPermission(permission: String, requestCode: Int)

        fun isPermissionGranted(permission: String): Boolean

    }

    interface Presenter<V: MainContract.View> : BasePresenter<V> {

        fun changeCamera()

        fun makePhoto()

        fun changeFlash()

        fun openGallery()

        fun start()

        fun stop()

    }

}