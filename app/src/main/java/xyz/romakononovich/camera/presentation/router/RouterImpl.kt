package xyz.romakononovich.camera.presentation.router

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION
import android.net.Uri
import android.os.StrictMode
import kotlinx.android.synthetic.main.switches_bottom_camera.*
import xyz.romakononovich.camera.R
import xyz.romakononovich.camera.presentation.facedetect.FaceDetectActivity
import xyz.romakononovich.camera.presentation.gallery.GalleryActivity
import xyz.romakononovich.camera.utils.IMAGE_JPEG
import xyz.romakononovich.camera.utils.INTENT_PATH
import xyz.romakononovich.camera.utils.ifElseLollipop
import java.io.File

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

    override fun sharePhoto(path: String) {
        val builder = StrictMode.VmPolicy.Builder() //https://stackoverflow.com/questions/48117511/exposed-beyond-app-through-clipdata-item-geturi
        StrictMode.setVmPolicy(builder.build())
        val uriToImage = Uri.fromFile(File(path))
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = IMAGE_JPEG
            putExtra(Intent.EXTRA_STREAM, uriToImage)
        }
        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_photo_text)))
    }

    override fun openFaceDetectActivity(path: String) {
        context.startActivity(Intent(context, FaceDetectActivity::class.java).addFlags(FLAG_ACTIVITY_NO_ANIMATION).putExtra(INTENT_PATH, path))
    }
}