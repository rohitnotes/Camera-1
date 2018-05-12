package xyz.romakononovich.camera.data.api

import android.content.Context
import android.graphics.*
import android.util.SparseArray
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector
import xyz.romakononovich.camera.R
import xyz.romakononovich.camera.domain.api.FaceDetectorApi
import javax.inject.Inject

/**
 * Created by RomanK on 12.05.18.
 */
class FacedetectorApiImpl
@Inject constructor(private val context: Context) : FaceDetectorApi {
    override var onFaceDetectError: (source: String) -> Unit = {}
    override var onFaceDetect: (bitmap: Bitmap) -> Unit = {}

    override fun start(path: String) {
        Glide.with(context)
                .asBitmap()
                .load(path)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        detectFace(resource)
                    }

                })
    }

    override fun stop() {
        faceDetector.release()
    }

    private companion object {
        const val CORNER_RADIUS = 2f
        const val STROKE_WIDTH = 2f
    }

    private lateinit var tempCanvas: Canvas
    private lateinit var tempBitmap: Bitmap

    private val rectPaint = Paint()

    private val faceDetector: FaceDetector
        get() = initializeFaceDetector()


    private fun initializeFaceDetector(): FaceDetector {
        return FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .build()
    }

    fun detectFace(bitmap: Bitmap) {

        processImage(bitmap)

        when (faceDetector.isOperational) {
            true -> {
                val frame = Frame
                        .Builder()
                        .setBitmap(bitmap)
                        .build()

                val sparseArray = faceDetector.detect(frame)

                loadDetectedResult(sparseArray)

                faceDetector.release()
            }
            false -> {
                onFaceDetectError.invoke(context.getString(R.string.device_no_support_face_detector))
            }
        }
        onFaceDetect.invoke(tempBitmap)
    }


    private fun loadDetectedResult(sparseArray: SparseArray<Face>) {
        for (i in 0 until sparseArray.size()) {
            val face = sparseArray.valueAt(i)

            val left = face.position.x
            val top = face.position.y
            val right = left + face.width
            val bottom = top + face.height

            val rectF = RectF(left, top, right, bottom)
            tempCanvas.drawRoundRect(rectF, CORNER_RADIUS, CORNER_RADIUS, rectPaint)
        }
    }

    private fun processImage(bitmap: Bitmap) {
        rectPaint.createRectanglePaint()
        tempBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        tempCanvas = Canvas(tempBitmap).apply {
            drawBitmap(bitmap, 0f, 0f, null)
        }
    }

    private fun Paint.createRectanglePaint() {
        this.apply {
            strokeWidth = STROKE_WIDTH
            color = Color.CYAN
            style = Paint.Style.STROKE
        }
    }
}