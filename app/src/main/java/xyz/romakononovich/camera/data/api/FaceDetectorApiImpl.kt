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
import xyz.romakononovich.camera.data.executor.MainThreadImpl
import xyz.romakononovich.camera.domain.api.FaceDetectorApi
import javax.inject.Inject

/**
 * Created by RomanK on 12.05.18.
 */
class FaceDetectorApiImpl
@Inject constructor(private val context: Context) : FaceDetectorApi {
    var stroke = 0f
    var cornerRadius = 0f
    private val mainThread = MainThreadImpl.instance

    override var onFaceDetectError: (source: String) -> Unit = {}
    override var onFaceDetect: (bitmap: Bitmap) -> Unit = {}
    override var onErrorNoFace: (source: String) -> Unit = {}


    override fun start(path: String) {
        Glide.with(context)
                .asBitmap()
                .load(path)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        stroke = resource.width / 250f
                        cornerRadius = resource.width / 150f
                        detectFace(resource)
                    }

                })
    }

    override fun stop() {
        faceDetector.release()
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
                Thread {
                    val frame = Frame
                            .Builder()
                            .setBitmap(bitmap)
                            .build()

                    val sparseArray = faceDetector.detect(frame)
                    if (sparseArray.size() == 0) {
                        mainThread?.post { onErrorNoFace.invoke(context.getString(R.string.face_no_found)) }
                    } else {
                        loadDetectedResult(sparseArray)
                        mainThread?.post { onFaceDetect.invoke(tempBitmap) }
                    }
                }.start()
                faceDetector.release()
            }
            false -> {
                onFaceDetectError.invoke(context.getString(R.string.device_no_support_face_detector))
            }
        }

    }


    private fun loadDetectedResult(sparseArray: SparseArray<Face>) {
        for (i in 0 until sparseArray.size()) {
            val face = sparseArray.valueAt(i)

            val left = face.position.x
            val top = face.position.y
            val right = left + face.width
            val bottom = top + face.height

            val rectF = RectF(left, top, right, bottom)
            tempCanvas.drawRoundRect(rectF, cornerRadius, cornerRadius, rectPaint)
        }
    }

    private fun processImage(bitmap: Bitmap) {
        rectPaint.createRectanglePaint()
        tempBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        tempCanvas = Canvas(tempBitmap).apply {
            drawBitmap(bitmap, 0f, 0f, null)
        }
//        tempBitmap.recycle()
    }

    private fun Paint.createRectanglePaint() {
        this.apply {
            strokeWidth = stroke
            color = Color.YELLOW
            style = Paint.Style.STROKE
        }
    }
}