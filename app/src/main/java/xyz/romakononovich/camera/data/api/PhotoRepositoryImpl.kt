package xyz.romakononovich.camera.data.api

import android.os.Environment
import xyz.romakononovich.camera.domain.api.PhotoRepository
import xyz.romakononovich.camera.utils.ALBUM_NAME
import java.io.File

/**
 * Created by RomanK on 10.05.18.
 */
class PhotoRepositoryImpl: PhotoRepository {

    override fun getListPhoto(): MutableList<String>  {
        val storageDir = File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), ALBUM_NAME).listFiles().reversedArray()
        val pathsList: MutableList<String> = ArrayList()
        for (image in storageDir) {
            pathsList.add(image.absolutePath)
        }
        return  pathsList
    }

    override fun deletePhoto(path: String) {
        File(path).delete()
    }
}