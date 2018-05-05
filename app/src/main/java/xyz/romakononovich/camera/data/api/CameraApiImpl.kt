package xyz.romakononovich.camera.data.api

import android.hardware.Camera
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import xyz.romakononovich.camera.catchAll
import xyz.romakononovich.camera.data.executor.MainThreadImpl
import xyz.romakononovich.camera.data.model.FlashMode
import xyz.romakononovich.camera.domain.api.CameraApi
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by RomanK on 05.05.18.
 */
class CameraApiImpl(private val appName: String) : CameraApi {

    private companion object {
        val TAG = CameraApiImpl::class.java.simpleName
    }

    private val mainThread = MainThreadImpl.instance
    private var camera: Camera? = null
    private var numberOfCameras = 0
    private var isFacingCameraPresent = false
    private var isBackCameraPresent = false
    private var facingCameraId = 0
    private var backCameraId = 0
    private var currentCameraId = 0
    private var currentFlashMode = FlashMode.NONE
    private val supportedFlashModes: MutableList<FlashMode> = mutableListOf()

    init {
        getCameras()
    }

    override var onCameraChanged: (camera: Camera) -> Unit = {}
    override var onCamerasRetrieved: (count: Int) -> Unit = {}
    override var onFlashModeChanged: (FlashMode) -> Unit = {}
    override var onPhotoSaved: (path: String) -> Unit = {}
    override var onPhotoSavedFail: () -> Unit = {}

    override fun start() {
        onCamerasRetrieved.invoke(numberOfCameras)
        if (camera == null) {
            openCamera(currentCameraId)
        }
    }

    override fun stop() {
        stopCameraPreview()
        releaseCamera()
    }

    override fun changeCamera() {
        if (isFacingCameraPresent && currentCameraId == backCameraId) {
            openCamera(facingCameraId)
        } else if (isBackCameraPresent && currentCameraId == facingCameraId) {
            openCamera(backCameraId)
        }
    }

    override fun makePhoto() {
        camera?.takePicture(null, null, pictureCallback)
    }

    override fun changeFlashMode() {
        if (currentFlashMode != FlashMode.NONE) {
            currentFlashMode = when (currentFlashMode) {
                FlashMode.ON -> FlashMode.AUTO
                FlashMode.AUTO -> FlashMode.OFF
                else -> FlashMode.ON
            }
            updateFlashMode(currentFlashMode)
        }
    }

    private fun getCameras() {
        numberOfCameras = Camera.getNumberOfCameras()
        for (i in 0 until numberOfCameras) {
            val info = Camera.CameraInfo()
            Camera.getCameraInfo(i, info)
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                isFacingCameraPresent = true
                facingCameraId = i
            } else if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                isBackCameraPresent = true
                backCameraId = i
            }
        }
        if (isBackCameraPresent) {
            currentCameraId = backCameraId
        } else if (isFacingCameraPresent) {
            currentCameraId = facingCameraId
        }
    }

    // https://stackoverflow.com/questions/16765527/android-switch-camera-when-button-clicked
    private fun openCamera(cameraId: Int) {
        currentCameraId = cameraId
        stopCameraPreview()
        releaseCamera()
        catchAll("open camera", {
            camera = Camera.open(cameraId)
            cameraChanged()
        })
        initFlashModes()
        initAutoFocus()
    }

    private fun initFlashModes() {
        currentFlashMode = FlashMode.NONE
        supportedFlashModes.clear()
        camera?.parameters?.supportedFlashModes?.forEach {
            when (it) {
                Camera.Parameters.FLASH_MODE_ON -> supportedFlashModes.add(FlashMode.ON)
                Camera.Parameters.FLASH_MODE_AUTO -> {
                    // check whether device supports AUTO flash, if support then set auto flash
                    currentFlashMode = FlashMode.AUTO
                    supportedFlashModes.add(FlashMode.AUTO)
                    updateFlashMode(currentFlashMode)
                }
                Camera.Parameters.FLASH_MODE_OFF -> supportedFlashModes.add(FlashMode.OFF)
            }
        }
        if (supportedFlashModes.size == 0 || supportedFlashModes.size == 1) {
            currentFlashMode = FlashMode.NONE
            onFlashModeChanged.invoke(currentFlashMode)
        }
    }

    private fun cameraChanged() {
        camera?.apply {
            setDisplayOrientation(90) // set orientation from camera
            onCameraChanged.invoke(this)
        }
    }

    private fun updateFlashMode(mode: FlashMode) {
        /*camera?.parameters?.flashMode = when (mode) {
            FlashMode.ON -> Camera.Parameters.FLASH_MODE_ON
            FlashMode.AUTO -> Camera.Parameters.FLASH_MODE_AUTO
            else -> Camera.Parameters.FLASH_MODE_OFF
        }*/
        camera?.parameters = camera?.parameters?.apply {
            flashMode = when (mode) {
                FlashMode.ON -> Camera.Parameters.FLASH_MODE_ON
                FlashMode.AUTO -> Camera.Parameters.FLASH_MODE_AUTO
                else -> Camera.Parameters.FLASH_MODE_OFF
            }
        }
        onFlashModeChanged.invoke(mode)
    }

    private fun initAutoFocus() {
        // set default focus mode FOCUS_MODE_CONTINUOUS_PICTURE if present
        camera?.parameters?.supportedFocusModes?.run {
            if (this.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                camera?.parameters = camera?.parameters?.apply {
                    focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
                }
            }
        }
    }

    private fun stopCameraPreview() {
        // stop preview before making changes
        catchAll("tried to stop a non-existent preview", {
            camera?.stopPreview()
        })
    }

    private fun releaseCamera() {
        camera?.release()
        camera = null
    }

    private val pictureCallback = Camera.PictureCallback { data, camera ->
        getOutputMediaFile(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE).let {
            // it -> picture file
            if (it == null) {
                Log.d(TAG, "Error creating media file, check storage permissions")
                onPhotoSavedFail.invoke()
                return@PictureCallback
            } else {
                // save photo in other thread
                Thread({
                    catchAll("error saving file", {
                        val fos = FileOutputStream(it)
                        fos.write(data)
                        fos.close()
                        mainThread?.post { onPhotoSaved.invoke(it.absolutePath) }
                    })
                }).start()

                this.camera = camera
                cameraChanged()
            }
        }
    }

    // Create a File for saving an image or video
    private fun getOutputMediaFile(type: Int): File? {
        val mediaStorageDir = File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), appName)
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
        return if (type == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
            File("${mediaStorageDir.path}${File.separator}IMG_$timeStamp.jpg")
        else
            File("${mediaStorageDir.path}${File.separator}VID_$timeStamp.mp4")
    }
}