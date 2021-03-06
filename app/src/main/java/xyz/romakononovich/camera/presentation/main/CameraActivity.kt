package xyz.romakononovich.camera.presentation.main

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Camera
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.switches_bottom_camera.*
import kotlinx.android.synthetic.main.switches_top_camera.*
import xyz.romakononovich.camera.R
import xyz.romakononovich.camera.presentation.base.BaseActivity
import xyz.romakononovich.camera.presentation.view.CameraPreview
import xyz.romakononovich.camera.utils.*
import java.util.*
import javax.inject.Inject

class CameraActivity : BaseActivity(), CameraContract.View, View.OnClickListener {

    @Inject
    lateinit var presenter: CameraPresenter<CameraContract.View>

    private lateinit var orientationEventListener: RotateOrientationEventListener
    private var orientationDegrees = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()
        presenter.onAttach(this)
    }

    override fun onResume() {
        super.onResume()
        orientationEventListener.checkDetectOrientation()
        if (isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            setPreviewLastPhoto(getPathLastPhoto())
        } else {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_PERMISSION_FOR_GET_LAST_PHOTO)
        }

        if (isPermissionGranted(Manifest.permission.CAMERA)) {
            presenter.start()
        } else {
            requestPermission(Manifest.permission.CAMERA, REQUEST_PERMISSION_CAMERA)
        }



    }

    // https://developer.android.com/training/permissions/requesting.html
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION_CAMERA -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    presenter.start()
                } else {
                    showCannotShowCameraToast()
                }
                return
            }
            REQUEST_PERMISSION_FOR_SAVE_PHOTO -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    presenter.makePhoto(orientationDegrees)
                } else {
                    showCannotSavePhotoToast()
                }
                return
            }
            REQUEST_PERMISSION_FOR_GET_LAST_PHOTO -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    setPreviewLastPhoto(getPathLastPhoto())
                } else {
                }
                return
            }
            REQUEST_PERMISSION_FOR_OPEN_GALLERY -> {
                if ((grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                    showCannotOpenGalleryToast()
                }
                return
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun initListener() {
        orientationEventListener = object : RotateOrientationEventListener(this@CameraActivity, SensorManager.SENSOR_DELAY_NORMAL) {
            override fun onRotateChanged(startDeg: Int, endDeg: Int) {
                val anim = RotateAnimation(startDeg.toFloat(), endDeg.toFloat(), Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
                orientationDegrees = endDeg
                anim.duration = 500L
                anim.fillAfter = true
                btnGrid.startRotate(anim)
                btnFlash.startRotate(anim)
                btnChangeCamera.startRotate(anim)
                ivPhotoCamera.startRotate(anim)
                ivLastPhoto.startRotate(anim)
            }
        }
        orientationEventListener.checkDetectOrientation()
    }

    override fun onPause() {
        super.onPause()
//        presenter.stop()
        orientationEventListener.disable()

    }

    override var onCameraChanged: (camera: Camera) -> Unit = {
        val cameraPreview = CameraPreview(this, it)
        preview.removeAllViews()
        preview.addView(cameraPreview)
    }

    override fun hideChangeCamera() {
        btnChangeCamera.visibility = View.GONE
    }

    override fun hideFlash() {
        btnFlash.clearAnimation()
        btnFlash.visibility = View.INVISIBLE
        btnFlash.visibility = View.INVISIBLE
    }

    override fun showFlashOn() {
        showFlash()
        btnFlash.setImageResource(R.drawable.ic_flash_on)
    }

    override fun showFlashAuto() {
        showFlash()
        btnFlash.setImageResource(R.drawable.ic_flash_auto)
    }

    override fun showFlashOff() {
        showFlash()
        btnFlash.setImageResource(R.drawable.ic_flash_off)
    }

    override fun showEmptyGalleryToast() {
        toast(getString(R.string.empty_gallery))
    }

    override fun showPhotoSavedToast(path: String) {
        // toast(getString(R.string.photo_saved, path))
    }

    override fun showCannotShowCameraToast() {
        toast(getString(R.string.cannot_show_camera))
    }

    override fun showCannotSavePhotoToast() {
        toast(getString(R.string.cannot_save_photo))
    }

    override fun showCannotOpenGalleryToast() {
        toast(getString(R.string.cannot_open_gallery_permission))
    }

    private fun showFlash() {
        btnFlash.visibility = View.VISIBLE
    }

    private fun changeStateGrid() {
        if (gridView.visibility == View.VISIBLE) {
            gridView.visibility = View.GONE
            btnGrid.setImageResource(R.drawable.ic_grid_on)
        } else {
            gridView.visibility = View.VISIBLE
            btnGrid.setImageResource(R.drawable.ic_grid_off)
        }
    }

    override fun unlockMakePhoto() {
        btnMakePhoto.isEnabled = true
    }

    override fun lockMakePhoto() {
        btnMakePhoto.isEnabled = false
    }

    override fun setPreviewLastPhoto(path: String?) {
        if (path != null) {
            Glide.with(this)
                    .load(path)
                    .transition(GenericTransitionOptions.with(R.anim.zoom_in))
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivLastPhoto)
        } else {
            Glide.with(this)
                    .load(R.drawable.ic_photo)
                    .into(ivLastPhoto)
        }

    }

    override fun getPathLastPhoto(): String? {
        if (!getSortedByNameListFiles().isEmpty()) {
            return getSortedByNameListFiles().first()
        }
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_PERMISSION_FOR_GET_LAST_PHOTO)

        return null
    }


    override fun makePhoto() {
        if (isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            lockMakePhoto()
            presenter.makePhoto(orientationDegrees)
        } else {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_PERMISSION_FOR_SAVE_PHOTO)
        }
    }

    override fun openGallery() {
        if (isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (getPathLastPhoto() != null) {
                presenter.openGallery()
            } else {
                showEmptyGalleryToast()
            }
        } else {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_PERMISSION_FOR_OPEN_GALLERY)
        }
    }

    override fun onClick(view: View) {
        when (view) {
            btnChangeCamera -> {
                btnChangeCamera.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate))
                presenter.changeCamera()
            }
            btnFlash -> {
                presenter.changeFlash()
            }
            btnMakePhoto -> {
                makePhoto()
            }
            btnGrid -> {
                changeStateGrid()
            }
            ivLastPhoto -> {
                openGallery()
            }

        }
    }

    override fun getResLayout(): Int {
        return R.layout.activity_main
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }
}
