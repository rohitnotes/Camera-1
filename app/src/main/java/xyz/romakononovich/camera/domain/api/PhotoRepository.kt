package xyz.romakononovich.camera.domain.api

/**
 * Created by RomanK on 10.05.18.
 */
interface PhotoRepository {

    fun getListPhoto(): MutableList<String>

    fun deletePhoto(path: String)
}