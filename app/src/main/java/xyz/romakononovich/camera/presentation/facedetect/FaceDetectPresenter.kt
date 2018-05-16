package xyz.romakononovich.camera.presentation.facedetect

import xyz.romakononovich.camera.domain.api.FaceDetectorApi
import xyz.romakononovich.camera.presentation.base.BasePresenterImpl
import xyz.romakononovich.camera.presentation.router.Router
import javax.inject.Inject

/**
 * Created by RomanK on 12.05.18.
 */
class FaceDetectPresenter<V : FaceDetectContract.View>
@Inject constructor(private val faceApi: FaceDetectorApi,
                    private val router: Router) : BasePresenterImpl<V>(), FaceDetectContract.Presenter<V> {



    init {
        faceApi.run {
            onFaceDetect = {
                view()?.onFaceDetect?.invoke(it)
            }

            onFaceDetectError = {
                view()?.onFaceDetectError?.invoke(it)
            }

            onErrorNoFace = {
                view()?.onErrorNoFace?.invoke(it)
            }
            onShowLandmarks = {
                view()?.onShowLandmarks?.invoke(it)
            }
            onPhotoSaved = {
                view()?.showPhotoSavedToast(it)
                view()?.closeActivity()
            }
            onPhotoSavedFail = {

            }

        }
    }

    override fun startFaceDetector(path: String) {
        faceApi.start(path)
    }

    override fun showLandmarks() {
        faceApi.detectLandmarks()
    }

    override fun savePhoto() {
        faceApi.savePhoto()
    }
    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stop() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}