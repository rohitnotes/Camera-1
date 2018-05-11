package xyz.romakononovich.camera.presentation.base

/**
 * Created by RomanK on 05.05.18.
 */
interface BaseView {

    fun requestPermission(permission: String, requestCode: Int)

    fun isPermissionGranted(permission: String): Boolean

}