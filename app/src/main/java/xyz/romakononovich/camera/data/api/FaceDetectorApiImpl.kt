package xyz.romakononovich.camera.data.api

import android.content.Context
import android.graphics.*
import android.os.Environment
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
import xyz.romakononovich.camera.utils.ALBUM_NAME
import xyz.romakononovich.camera.utils.catchAll
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by RomanK on 12.05.18.
 */
class FaceDetectorApiImpl
@Inject constructor(private val context: Context) : FaceDetectorApi {

    companion object {
        const val LEFT_EYE = 4
        const val RIGHT_EYE = 10
    }

    private lateinit var eyePatchBitmap: Bitmap
    private lateinit var originalBitmap: Bitmap
    private lateinit var resizeEyeBitmap: Bitmap
    private lateinit var sparseArray: SparseArray<Face>
    private lateinit var tempCanvas: Canvas
    private lateinit var tempBitmapRect: Bitmap
    private lateinit var tempBitmapEye: Bitmap
    private val mainThread = MainThreadImpl.instance
    private val rectPaint = Paint()
    private var stroke = 0f
    private var cornerRadius = 0f
    private val faceDetector: FaceDetector
        get() = initializeFaceDetector()

    override var onFaceDetectError: (source: String) -> Unit = {}
    override var onFaceShow: (bitmap: Bitmap) -> Unit = {}
    override var onFaceDetect: () -> Unit = {}
    override var onErrorNoFace: (source: String) -> Unit = {}
    override var onShowLandmarks: (bitmap: Bitmap) -> Unit = {}
    override var onPhotoSaved: (path: String) -> Unit = {}
    override var onPhotoSavedFail: () -> Unit = {}

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


    private fun initializeFaceDetector(): FaceDetector {
        return FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build()
    }

    private fun detectFace(bitmap: Bitmap) {
        originalBitmap = bitmap
        processImageRect(bitmap)

        when (faceDetector.isOperational) {
            true -> {
                Thread {
                    val frame = Frame
                            .Builder()
                            .setBitmap(bitmap)
                            .build()

                    sparseArray = faceDetector.detect(frame)
                    if (sparseArray.size() == 0) {
                        mainThread?.post { onErrorNoFace.invoke(context.getString(R.string.face_no_found)) }
                    } else {
                        mainThread?.post { onFaceDetect.invoke() }
                        loadDetectedResult(sparseArray)
                        mainThread?.post { onFaceShow.invoke(tempBitmapRect) }
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

    override fun detectLandmarks() {
        processImageEye(originalBitmap)
        for (i in 0 until sparseArray.size()) {
            val face = sparseArray.valueAt(i)
            resizeEyeBitmap = Bitmap.createScaledBitmap(eyePatchBitmap, (face.width / 4).toInt(), (face.width / 4).toInt(), false)

            for (landmark in face.landmarks) {
                val xCoordinate = landmark.position.x
                val yCoordinate = landmark.position.y
                drawEyePatchBitmap(landmark.type, xCoordinate, yCoordinate)
            }
        }
        onShowLandmarks.invoke(tempBitmapEye)
    }


    private fun drawEyePatchBitmap(landmarkType: Int, xCoordinate: Float, yCoordinate: Float) {
        when (landmarkType) {
            LEFT_EYE, RIGHT_EYE -> {
                // TODO: Optimize so that this calculation is not done for every face
                val scaledWidth = resizeEyeBitmap.getScaledWidth(tempCanvas)
                val scaledHeight = resizeEyeBitmap.getScaledHeight(tempCanvas)
                tempCanvas.drawBitmap(resizeEyeBitmap,
                        xCoordinate - scaledWidth / 2,
                        yCoordinate - scaledHeight / 2,
                        null)
            }
        }
    }

    private fun processImageRect(bitmap: Bitmap) {
        rectPaint.createRectanglePaint()
        tempBitmapRect = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        tempCanvas = Canvas(tempBitmapRect).apply {
            drawBitmap(bitmap, 0f, 0f, null)
        }
    }

    private fun processImageEye(bitmap: Bitmap) {
        tempBitmapEye = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        tempCanvas = Canvas(tempBitmapEye).apply {
            drawBitmap(bitmap, 0f, 0f, null)
        }
        eyePatchBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.eye)
    }

    private fun Paint.createRectanglePaint() {
        this.apply {
            strokeWidth = stroke
            color = Color.YELLOW
            style = Paint.Style.STROKE
        }
    }

    override fun savePhoto(){
        getOutputMediaFile().let {
            // it -> picture file
            if (it == null) {
                onPhotoSavedFail.invoke()
                return
            } else {
                // save photo in other thread
                Thread({
                    catchAll("error saving file", {
                        val fos = FileOutputStream(it)
                        tempBitmapEye.compress(Bitmap.CompressFormat.JPEG, 85, fos)
                        fos.flush()
                        fos.close()
                        mainThread?.post { onPhotoSaved.invoke(it.absolutePath) }
                    })
                }).start()
            }
        }
    }
    private fun getOutputMediaFile(): File? {
        val mediaStorageDir = File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), ALBUM_NAME)
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }

        // Create a media file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        return File("${mediaStorageDir.path}${File.separator}IMG_$timeStamp.jpg")
    }
}