package xyz.romakononovich.camera.presentation.gallery

import android.os.Bundle
import android.os.Environment
import kotlinx.android.synthetic.main.activity_gallery.*
import xyz.romakononovich.camera.R
import xyz.romakononovich.camera.presentation.base.BaseActivity
import xyz.romakononovich.camera.utils.DepthPageTransformer
import java.io.File

/**
 * Created by RomanK on 06.05.18.
 */

class GalleryActivity : BaseActivity() {

    private val pathsList: MutableList<String> = ArrayList()
    private var galleryAdapter: GalleryAdapter? = null

    private val storageDir: File by lazy {
        File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Camera")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        for (image in storageDir.listFiles().reversedArray()) {
            pathsList.add(image.absolutePath)
        }
        galleryAdapter = GalleryAdapter(this, pathsList)
        viewPager.adapter = galleryAdapter
        viewPager.setPageTransformer(true, DepthPageTransformer())

    }

    override fun getResLayout(): Int {
        return R.layout.activity_gallery
    }

}

