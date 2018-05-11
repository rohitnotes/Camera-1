package xyz.romakononovich.camera.presentation.router

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.StrictMode
import xyz.romakononovich.camera.R
import xyz.romakononovich.camera.utils.IMAGE_JPEG
import java.io.File

/**
 * Created by RomanK on 11.05.18.
 */
class RouterGalleryImpl(private val context: Context) : RouterGallery{

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

}