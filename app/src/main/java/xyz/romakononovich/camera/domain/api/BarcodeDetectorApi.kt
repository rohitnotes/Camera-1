package xyz.romakononovich.camera.domain.api

/**
 * Created by RomanK on 10.05.18.
 */
interface BarcodeDetectorApi {
    var onBarcodeDetect: (source: String) -> Unit

    fun start(path: String)

    fun stop()
}