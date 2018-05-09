package xyz.romakononovich.camera.presentation.router

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import kotlinx.android.synthetic.main.switches_bottom_camera.*
import xyz.romakononovich.camera.R
import xyz.romakononovich.camera.presentation.gallery.GalleryActivity
import xyz.romakononovich.camera.utils.ifElseLollipop

/**
 * Created by RomanK on 06.05.18.
 */
class RouterImpl(private val context: Context) : Router {
    override fun openGallery() {
        ifElseLollipop({
            context.startActivity(Intent(context, GalleryActivity::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(context as Activity,
                            context.ivLastPhoto,
                            context.getString(R.string.description_photo_in_gallery)).toBundle())
        }, {
            context.startActivity(Intent(context, GalleryActivity::class.java))
        })
    }
}