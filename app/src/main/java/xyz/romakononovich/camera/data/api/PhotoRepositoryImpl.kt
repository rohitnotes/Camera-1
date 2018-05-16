package xyz.romakononovich.camera.data.api

import android.content.Context
import android.media.ExifInterface
import android.text.format.DateUtils
import android.text.format.Formatter
import xyz.romakononovich.camera.R
import xyz.romakononovich.camera.domain.api.PhotoRepository
import xyz.romakononovich.camera.utils.getSortedByNameListFiles
import java.io.File
import java.util.*
import javax.inject.Inject

/**
 * Created by RomanK on 10.05.18.
 */
class PhotoRepositoryImpl
@Inject constructor(private val context: Context) : PhotoRepository {
    override var onGetInfo: (source: String) -> Unit = {}

    override fun getListPhoto(): MutableList<String>  = getSortedByNameListFiles()

    override fun deletePhoto(path: String) {
        File(path).delete()
    }

    override fun getExif(path: String) {
        val exif = ExifInterface(path)
        val file = File(path)
        val builder = StringBuilder()
        builder.append("${context.getString(R.string.dialog_info_name)}: ${file.nameWithoutExtension}\n\n")
        builder.append("${context.getString(R.string.dialog_info_time)}: ${DateUtils.formatDateTime(context, file.lastModified(), DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME)}\n\n")
        builder.append("${context.getString(R.string.dialog_info_height)}: ${exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)}\n\n")
        builder.append("${context.getString(R.string.dialog_info_width)}: ${exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)}\n\n")
        builder.append("${context.getString(R.string.dialog_info_author)}: ${exif.getAttribute(ExifInterface.TAG_MAKE)}\n\n")
        builder.append("${context.getString(R.string.dialog_info_model)}: ${exif.getAttribute(ExifInterface.TAG_MODEL)}\n\n")
        builder.append("${context.getString(R.string.dialog_info_size)}: ${Formatter.formatFileSize(context, file.length())}\n\n")
        builder.append("${context.getString(R.string.dialog_info_path)}: ${file.absolutePath}\n\n")
        onGetInfo.invoke(builder.toString())
    }
}