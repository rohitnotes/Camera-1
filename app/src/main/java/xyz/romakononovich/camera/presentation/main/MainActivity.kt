package xyz.romakononovich.camera.presentation.main

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.animation.AnimationUtils
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.switches_bottom.*
import kotlinx.android.synthetic.main.switches_top.*
import xyz.romakononovich.camera.R
import xyz.romakononovich.camera.data.api.CameraApiImpl
import xyz.romakononovich.camera.presentation.base.BaseActivity
import xyz.romakononovich.camera.presentation.router.RouterImpl
import xyz.romakononovich.camera.presentation.view.CameraPreview
import xyz.romakononovich.camera.utils.toast
import java.io.File

class MainActivity : BaseActivity(), MainContract.View, View.OnClickListener {

    private companion object {
        const val PERMISSION_CAMERA = Manifest.permission.CAMERA
        const val REQUEST_PERMISSION_CAMERA = 0
        const val PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
        const val REQUEST_PERMISSION_FOR_SAVE_PHOTO = 1
        const val REQUEST_PERMISSION_FOR_GET_LAST_PHOTO = 2
        const val REQUEST_PERMISSION_FOR_OPEN_GALLERY = 3
    }

    override var presenter: MainContract.Presenter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainPresenter(this, CameraApiImpl(getString(R.string.app_name)), RouterImpl(this)) // init presenter
        setPreviewLastPhoto(getPathLastPhoto())
    }

    override fun onResume() {
        super.onResume()
        if (isPermissionGranted(PERMISSION_CAMERA)) {
            presenter?.start()
        } else {
            requestPermission(PERMISSION_CAMERA, REQUEST_PERMISSION_CAMERA)
        }
    }

    // https://developer.android.com/training/permissions/requesting.html
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION_CAMERA -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    presenter?.start()
                } else {
                    showCannotShowCameraToast()
                }
                return
            }
            REQUEST_PERMISSION_FOR_SAVE_PHOTO -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    presenter?.makePhoto()
                } else {
                    showCannotSavePhotoToast()
                }
                return
            }
            REQUEST_PERMISSION_FOR_GET_LAST_PHOTO -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    setPreviewLastPhoto(getPathLastPhoto())
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


    override fun onPause() {
        super.onPause()
        presenter?.stop()
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
        btnFlash.visibility = View.GONE
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

    override fun showPhotoSavedToast(path: String) {
        toast(getString(R.string.photo_saved, path))
    }

    override fun showCannotShowCameraToast() {
        toast(getString(R.string.cannot_show_camera))
    }

    override fun showCannotSavePhotoToast() {
        toast(getString(R.string.cannot_save_photo))
    }

    override fun showCannotOpenGalleryToast() {
        toast(getString(R.string.cannot_open_gallery))
    }

    override fun requestPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(permission),
                    requestCode)
        }
    }

    override fun isPermissionGranted(permission: String) =
            ContextCompat.checkSelfPermission(this@MainActivity, permission) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP

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
        return if (isPermissionGranted(PERMISSION_WRITE_EXTERNAL_STORAGE)) {
            File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "Camera").listFiles().last().absolutePath
        } else {
            requestPermission(PERMISSION_WRITE_EXTERNAL_STORAGE, REQUEST_PERMISSION_FOR_GET_LAST_PHOTO)
            null
        }

    }

    override fun makePhoto() {
        if (isPermissionGranted(PERMISSION_WRITE_EXTERNAL_STORAGE)) {
            lockMakePhoto()
            presenter?.makePhoto()
        } else {
            requestPermission(PERMISSION_WRITE_EXTERNAL_STORAGE, REQUEST_PERMISSION_FOR_SAVE_PHOTO)
        }
    }

    override fun openGallery() {
        if (isPermissionGranted(PERMISSION_WRITE_EXTERNAL_STORAGE)) {
            presenter?.openGallery()
        } else {
            requestPermission(PERMISSION_WRITE_EXTERNAL_STORAGE, REQUEST_PERMISSION_FOR_OPEN_GALLERY)
        }
    }

    override fun onClick(view: View) {
        when (view) {
            btnChangeCamera -> {
                btnChangeCamera.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate))
                presenter?.changeCamera()
            }
            btnFlash -> {
                presenter?.changeFlash()
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
}
