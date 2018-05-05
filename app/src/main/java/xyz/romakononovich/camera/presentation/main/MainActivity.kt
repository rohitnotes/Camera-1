package xyz.romakononovich.camera.presentation.main

import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.switches_bottom.*
import kotlinx.android.synthetic.main.switches_top.*
import xyz.romakononovich.camera.R
import xyz.romakononovich.camera.data.api.CameraApiImpl
import xyz.romakononovich.camera.presentation.view.CameraPreview
import xyz.romakononovich.camera.toast

class MainActivity : AppCompatActivity(), MainContract.View {

    override var presenter: MainContract.Presenter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        MainPresenter(this, CameraApiImpl(getString(R.string.app_name))) // init presenter
        btnChangeCamera.setOnClickListener {
            btnChangeCamera.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate))
            presenter?.changeCamera()
        }
        btnFlash.setOnClickListener {
            presenter?.changeFlash()
        }
        btnMakePhoto.setOnClickListener {
            presenter?.makePhoto()
        }

        btnGrid.setOnClickListener {
            changeStateGrid()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter?.start()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        presenter?.permissionsResult(requestCode, permissions, grantResults)
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
        // imageButtonChangeCamera.visibility = View.GONE
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

    override fun requestPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            /*if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {*/
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    arrayOf(permission),
                    requestCode)
//            }
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

}
