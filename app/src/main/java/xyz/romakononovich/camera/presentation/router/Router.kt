package xyz.romakononovich.camera.presentation.router

/**
 * Created by RomanK on 06.05.18.
 */
interface Router {
    fun openGallery()

    fun sharePhoto(path: String)

    fun openFacedetectActivity(path: String)
}