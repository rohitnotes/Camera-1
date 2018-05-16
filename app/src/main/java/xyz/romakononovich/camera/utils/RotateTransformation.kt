package xyz.romakononovich.camera.utils

import android.graphics.Bitmap
import android.graphics.Matrix
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest


/**
 * Created by RomanK on 07.05.18.
 */
class RotateTransformation(rotateRotationAngle: Float) : BitmapTransformation() {

    // TODO Для глайда. Можно удалить
    private var rotate = 0f

    init {
        rotate = rotateRotationAngle
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val matrix = Matrix()

        matrix.postRotate(rotate)

        return Bitmap.createBitmap(toTransform, 0, 0, toTransform.width, toTransform.height, matrix, true)
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update("rotate$rotate".toByteArray())
    }
}