package xyz.romakononovich.camera.di.activity

import android.content.Context
import dagger.Module
import dagger.Provides
import xyz.romakononovich.camera.data.api.BarcodeDetectorApiImpl
import xyz.romakononovich.camera.data.api.CameraApiImpl
import xyz.romakononovich.camera.data.api.PhotoRepositoryImpl
import xyz.romakononovich.camera.domain.api.BarcodeDetectorApi
import xyz.romakononovich.camera.domain.api.CameraApi
import xyz.romakononovich.camera.domain.api.PhotoRepository
import xyz.romakononovich.camera.presentation.gallery.GalleryActivity
import xyz.romakononovich.camera.presentation.router.Router
import xyz.romakononovich.camera.presentation.router.RouterImpl
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by RomanK on 11.05.18.
 */
@Module
class GalleryActivityModule {
    @Provides
    fun provideRouter(activity: GalleryActivity): Router = RouterImpl(activity)
}