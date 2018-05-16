package xyz.romakononovich.camera.domain.api

/**
 * Created by RomanK on 10.05.18.
 */
interface PhotoApi {

    var onGetInfo: (source: String) -> Unit

    fun getListPhoto(): MutableList<String>

    fun deletePhoto(path: String)

    fun getExif(path: String)
}