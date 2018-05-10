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
import xyz.romakononovich.camera.presentation.base.BaseActivity
import xyz.romakononovich.camera.utils.DepthPageTransformer
import java.io.File

/**
 * Created by RomanK on 06.05.18.
 */

class GalleryActivity : BaseActivity(), GalleryContract.View, GalleryAdapter.ClickListener {

    override var presenter: GalleryContract.Presenter? = null
    private val pathsList: MutableList<String> = ArrayList()
    private var galleryAdapter: GalleryAdapter? = null

    private val storageDir: File by lazy {
        File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Camera")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GalleryPresenter(this, BarcodeDetectorApiImpl(this))


        for (image in storageDir.listFiles().reversedArray()) {
            pathsList.add(image.absolutePath)
        }
        galleryAdapter = GalleryAdapter(this, pathsList, this)
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


    override fun sharePhoto(imagePath: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deletePhoto(imagePath: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun faceDetectPhoto(imagePath: String) {
        btnFace.setOnClickListener {
            // presenter?.startFaceDetector(imagePath)
//            presenter?.startFaceDetector(storageDir.listFiles().reversedArray()[viewPager.currentItem].absolutePath)
        }


    }

    override fun barcodePhoto(imagePath: String) {
        btnQrCode.setOnClickListener {
            presenter?.startBarcodeDetector(storageDir.listFiles().reversedArray()[viewPager.currentItem].absolutePath)
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

