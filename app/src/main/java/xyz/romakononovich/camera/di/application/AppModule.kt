package xyz.romakononovich.camera.di.application

import android.content.Context
import dagger.Module
import dagger.Provides
import xyz.romakononovich.camera.data.api.BarcodeDetectorApiImpl
import xyz.romakononovich.camera.data.api.CameraApiImpl
import xyz.romakononovich.camera.data.api.PhotoRepositoryImpl
import xyz.romakononovich.camera.domain.api.BarcodeDetectorApi
import xyz.romakononovich.camera.domain.api.CameraApi
import xyz.romakononovich.camera.domain.api.PhotoRepository
import xyz.romakononovich.camera.presentation.gallery.GalleryContract
import xyz.romakononovich.camera.presentation.gallery.GalleryPresenter
import xyz.romakononovich.camera.presentation.main.MainContract
import xyz.romakononovich.camera.presentation.main.MainPresenter
import xyz.romakononovich.camera.presentation.router.Router
import xyz.romakononovich.camera.presentation.router.RouterGallery
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
    fun provideGalleryPresenter(barcodeApi: BarcodeDetectorApi, repository: PhotoRepository, router: RouterGallery): GalleryContract.Presenter<*> = GalleryPresenter<GalleryContract.View>(barcodeApi, repository, router)

    @Singleton
    @Provides
    fun provideCameraApi(): CameraApi = CameraApiImpl()

    @Singleton
    @Provides
    fun providePhotoRepository(): PhotoRepository = PhotoRepositoryImpl()

    @Singleton
    @Provides
    fun provideBarcodeApi(context: Context): BarcodeDetectorApi = BarcodeDetectorApiImpl(context)

}