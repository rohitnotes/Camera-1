package xyz.romakononovich.camera.presentation.gallery

import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlinx.android.synthetic.main.switches_bottom_gallery.*
import kotlinx.android.synthetic.main.toolbar_gallery.*
import xyz.romakononovich.camera.R
import xyz.romakononovich.camera.presentation.base.BaseActivity
import xyz.romakononovich.camera.presentation.view.DeleteDialog
import xyz.romakononovich.camera.presentation.view.InfoDialog
import xyz.romakononovich.camera.presentation.view.QrCodeDialog
import xyz.romakononovich.camera.utils.*
import javax.inject.Inject
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.support.v4.print.PrintHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition


/**
 * Created by RomanK on 06.05.18.
 */

class GalleryActivity : BaseActivity(),
        GalleryContract.View,
        GalleryAdapter.ClickListener,
        View.OnClickListener,
        DeleteDialog.DeleteDialogListener {


    @Inject
    lateinit var presenter: GalleryPresenter<GalleryContract.View>
    private var galleryAdapter: GalleryAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbarGallery)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
        presenter.onAttach(this)

        if (isPermissionGranted(PERMISSION_WRITE_EXTERNAL_STORAGE)) {
            presenter.start()
        } else {
            requestPermission(PERMISSION_WRITE_EXTERNAL_STORAGE, REQUEST_PERMISSION_FOR_OPEN_GALLERY)
        }

        includeToolbar.visibility = View.VISIBLE
        includeLayoutBottom.visibility = View.VISIBLE

    }

    override fun initViewPager(list: MutableList<String>) {
        galleryAdapter = GalleryAdapter(this, list, this)
        viewPager.adapter = galleryAdapter
        viewPager.setPageTransformer(true, DepthPageTransformer())
        viewPager.setOnClickListener(this)

    }

    override fun refreshListPager(list: MutableList<String>) {
        galleryAdapter?.refresh(list)

    }

    override var onBarcodeDetect: (source: String) -> Unit = {
        QrCodeDialog.newInstance(it).show(supportFragmentManager, QRCODE_DIALOG)
    }

    override var onGetInfo: (source: String) -> Unit = {
        InfoDialog.newInstance(it).show(supportFragmentManager, INFO_DIALOG)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION_FOR_OPEN_GALLERY -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    presenter.start()
                } else {
                    showCannotOpenGalleryToast()
                    onBackPressed()
                }
                return
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun showCannotOpenGalleryToast() {
        toast(getString(R.string.cannot_open_gallery))
    }


    override fun printPhoto(path: String) {
        val photoPrinter = PrintHelper(this)
        photoPrinter.scaleMode = PrintHelper.SCALE_MODE_FIT
        Glide.with(this)
                .asBitmap()
                .load(path)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        photoPrinter.printBitmap(path, resource)
                    }
                })
    }

    override fun onDeleteDialogPositiveClick(id: Int) {
        presenter.deletePhoto(id)
        galleryAdapter?.delete(id)
    }

    override fun onClick(view: View) {
        when (view) {
            btnQrCode -> {
                presenter.startBarcodeDetector(viewPager.currentItem)
            }
            btnDelete -> {
                DeleteDialog.newInstance(viewPager.currentItem).show(supportFragmentManager, DELETE_DIALOG)
            }
            btnShare -> {
                presenter.sharePhoto(viewPager.currentItem)
            }
            btnFace -> {
                presenter.openFaceDetectActivity(viewPager.currentItem)
            }
            btnInfo -> {
                presenter.showInfoPhoto(viewPager.currentItem)
            }
            btnPrint -> {
                presenter.printPhoto(viewPager.currentItem)
            }
        }
    }

    override fun getResLayout(): Int {
        return R.layout.activity_gallery
    }

    private fun showView(isShow: Boolean) {
        if (isShow) {
            ObjectAnimator.ofFloat(includeToolbar, "translationY", 0f)
                    .setDuration(300)
                    .start()
            includeToolbar.visibility = View.VISIBLE
            includeLayoutBottom.visibility = View.VISIBLE
        } else {
            ObjectAnimator.ofFloat(includeToolbar, "translationY", -includeToolbar.height.toFloat())
                    .setDuration(300)
                    .start()
            includeToolbar.visibility = View.INVISIBLE
            includeLayoutBottom.visibility = View.INVISIBLE
        }
    }

    override fun onClickViewPager() {
        if (includeToolbar.visibility == View.VISIBLE) {
            showView(false)
        } else {
            showView(true)
        }
    }

    override fun onLastPageDelete() {
        finish()
    }

    override fun onBackPressed() {
        if (includeToolbar.visibility == View.VISIBLE) {
            showView(false)
        }
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        presenter.refreshList()
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }
}

