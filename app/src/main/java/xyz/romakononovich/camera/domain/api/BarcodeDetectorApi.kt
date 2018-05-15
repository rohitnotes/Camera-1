package xyz.romakononovich.camera.domain.api

import com.google.android.gms.vision.barcode.BarcodeDetector

/**
 * Created by RomanK on 10.05.18.
 */
interface BarcodeDetectorApi {

    var onBarcodeDetect: (source: String) -> Unit

    fun initializeBarcodeDetector(): BarcodeDetector

    fun start(path: String)

    fun stop()
}