package xyz.romakononovich.camera.presentation.facedetect

import android.graphics.Bitmap
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_facedetect.*
import xyz.romakononovich.camera.R
import xyz.romakononovich.camera.presentation.base.BaseActivity
import xyz.romakononovich.camera.presentation.view.QrCodeDialog
import xyz.romakononovich.camera.utils.INTENT_PATH
import xyz.romakononovich.camera.utils.QRCODE_DIALOG
import javax.inject.Inject

/**
 * Created by RomanK on 12.05.18.
 */
class FaceDetectActivity : BaseActivity(),
        FaceDetectContract.View {

    @Inject
//    @Named("FaceDetectActivity")
    lateinit var presenter: FaceDetectPresenter<FaceDetectContract.View>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onAttach(this)
        Glide.with(this)
                .load(getPath())
                .into(ivFaceDetect)
        presenter.startFaceDetector(getPath())

    }

    override var onFaceDetect: (bitmap: Bitmap) -> Unit = {
        Glide.with(this)
                .load(it)
                .into(ivFaceDetect)
    }

    override var onFaceDetectError: (source: String) -> Unit = {
        QrCodeDialog.newInstance(it).show(supportFragmentManager, QRCODE_DIALOG)

    }

    override fun showDetectFace() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getResLayout(): Int {
        return R.layout.activity_facedetect
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }


    private fun getPath() = intent.extras.getString(INTENT_PATH)
}