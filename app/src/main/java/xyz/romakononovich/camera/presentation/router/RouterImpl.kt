package xyz.romakononovich.camera.presentation.router

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import kotlinx.android.synthetic.main.switches_bottom_camera.*
import xyz.romakononovich.camera.R
import xyz.romakononovich.camera.presentation.gallery.GalleryActivity
import xyz.romakononovich.camera.utils.ifElseLollipop

/**
 * Created by RomanK on 06.05.18.
 */
class RouterImpl(private val activity: Activity) : Router {

    override fun openGallery() {
        activity.startActivity(Intent(activity, GalleryActivity::class.java))
//        ifElseLollipop({
//            activity.startActivity(Intent(activity, GalleryActivity::class.java),
//                    ActivityOptions.makeSceneTransitionAnimation(activity,
//                            activity.ivLastPhoto,
//                            activity.getString(R.string.description_photo_in_gallery)).toBundle())
//        }, {
//            activity.startActivity(Intent(activity, GalleryActivity::class.java))
//        })
    }
}