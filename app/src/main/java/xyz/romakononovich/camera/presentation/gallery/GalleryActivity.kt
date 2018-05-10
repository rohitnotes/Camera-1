package xyz.romakononovich.camera.presentation.gallery

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.customtabs.CustomTabsIntent
import android.support.v7.app.AlertDialog
import android.view.View
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlinx.android.synthetic.main.item_pager.*
import kotlinx.android.synthetic.main.switches_bottom_gallery.*
import xyz.romakononovich.camera.R
import xyz.romakononovich.camera.data.api.BarcodeDetectorApiImpl
import xyz.romakononovich.camera.data.api.PhotoRepositoryImpl
import xyz.romakononovich.camera.presentation.base.BaseActivity
import xyz.romakononovich.camera.presentation.router.RouterImpl
import xyz.romakononovich.camera.utils.ALBUM_NAME
import xyz.romakononovich.camera.utils.DepthPageTransformer
import java.io.File

/**
 * Created by RomanK on 06.05.18.
 */

class GalleryActivity : BaseActivity(), GalleryContract.View, GalleryAdapter.ClickListener, View.OnClickListener {

    override var presenter: GalleryContract.Presenter? = null
    private var galleryAdapter: GalleryAdapter? = null

    private val storageDir: File by lazy {
        File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), ALBUM_NAME)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GalleryPresenter(this, BarcodeDetectorApiImpl(this), PhotoRepositoryImpl(), RouterImpl(this))
        presenter?.start()
    }

    override fun initViewPager(list: MutableList<String>) {
        galleryAdapter = GalleryAdapter(this, list, this)
        viewPager.adapter = galleryAdapter
        viewPager.setPageTransformer(true, DepthPageTransformer())
    }

    override var onBarcodeDetect: (source: String) -> Unit = {
        val dlg = AlertDialog.Builder(this)
                .setMessage(it)
                .setNeutralButton(getString(R.string.dialog_btn_close), null)
        if (it.startsWith("http://") || it.startsWith("https://")) {
            dlg.setPositiveButton(getString(R.string.dialog_btn_open_web), { _, _ ->
                CustomTabsIntent.Builder().build().launchUrl(this, Uri.parse(it))
            })
        }
        dlg.create().show()
    }


    override var onFaceDetect: (bitmap: Bitmap) -> Unit = {
        ivPhoto.setImageDrawable(BitmapDrawable(resources, it))
    }

    override fun showDetectFace() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun sharePhoto() {
        btnShare.setOnClickListener(this)
    }

    override fun deletePhoto() {
        btnDelete.setOnClickListener (this)
    }

    override fun faceDetectPhoto() {
        btnFace.setOnClickListener(this)
    }

    override fun barcodePhoto() {
        btnQrCode.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view) {
            btnQrCode -> {
                presenter?.startBarcodeDetector(storageDir.listFiles().reversedArray()[viewPager.currentItem].absolutePath)
            }
            btnDelete -> {
                presenter?.deletePhoto(viewPager.currentItem)
                galleryAdapter?.delete(viewPager.currentItem)
            }
            btnShare -> {
                presenter?.sharePhoto(viewPager.currentItem)
            }

        }
    }

    override fun getResLayout(): Int {
        return R.layout.activity_gallery
    }

    override fun onResume() {
        super.onResume()
        includeLayoutBottom.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        includeLayoutBottom.visibility = View.GONE
        super.onBackPressed()
    }
}

