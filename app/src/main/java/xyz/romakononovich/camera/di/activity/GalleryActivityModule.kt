package xyz.romakononovich.camera.di.activity

import dagger.Module
import dagger.Provides
import xyz.romakononovich.camera.presentation.gallery.GalleryActivity
import xyz.romakononovich.camera.presentation.router.Router
import xyz.romakononovich.camera.presentation.router.RouterImpl

/**
 * Created by RomanK on 11.05.18.
 */
@Module
class GalleryActivityModule {
    @Provides
    fun provideRouter(activity: GalleryActivity): Router = RouterImpl(activity)
}