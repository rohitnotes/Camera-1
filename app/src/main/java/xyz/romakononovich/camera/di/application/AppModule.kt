package xyz.romakononovich.camera.di.application

import android.content.Context
import dagger.Module
import dagger.Provides
import xyz.romakononovich.camera.data.api.BarcodeDetectorApiImpl
import xyz.romakononovich.camera.data.api.CameraApiImpl
import xyz.romakononovich.camera.data.api.FacedetectorApiImpl
import xyz.romakononovich.camera.data.api.PhotoRepositoryImpl
import xyz.romakononovich.camera.domain.api.BarcodeDetectorApi
import xyz.romakononovich.camera.domain.api.CameraApi
import xyz.romakononovich.camera.domain.api.FaceDetectorApi
import xyz.romakononovich.camera.domain.api.PhotoRepository
import xyz.romakononovich.camera.presentation.facedetect.FaceDetectContract
import xyz.romakononovich.camera.presentation.facedetect.FaceDetectPresenter
import xyz.romakononovich.camera.presentation.gallery.GalleryContract
import xyz.romakononovich.camera.presentation.gallery.GalleryPresenter
import xyz.romakononovich.camera.presentation.main.MainContract
import xyz.romakononovich.camera.presentation.main.MainPresenter
import xyz.romakononovich.camera.presentation.router.Router
import javax.inject.Singleton

/**
 * Created by RomanK on 11.05.18.
 */
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideMainPresenter(cameraApi: CameraApi, router: Router): MainContract.Presenter<*> = MainPresenter<MainContract.View>(cameraApi, router)

    @Singleton
    @Provides
    fun provideGalleryPresenter(barcodeApi: BarcodeDetectorApi, repository: PhotoRepository, router: Router): GalleryContract.Presenter<*> = GalleryPresenter<GalleryContract.View>(barcodeApi, repository, router)

    @Singleton
    @Provides
    fun provideFaceDetectPresenter(faceApi: FaceDetectorApi, router: Router): FaceDetectContract.Presenter<*> = FaceDetectPresenter<FaceDetectContract.View>(faceApi, router)


    @Singleton
    @Provides
    fun provideBarcodeApi(context: Context): BarcodeDetectorApi = BarcodeDetectorApiImpl(context)


    @Singleton
    @Provides
    fun providePhotoRepository(context: Context): PhotoRepository = PhotoRepositoryImpl(context)

    @Singleton
    @Provides
    fun provideCameraApi(): CameraApi = CameraApiImpl()

    @Singleton
    @Provides
    fun provideFaceDetectorApi(context: Context): FaceDetectorApi = FacedetectorApiImpl(context)
}