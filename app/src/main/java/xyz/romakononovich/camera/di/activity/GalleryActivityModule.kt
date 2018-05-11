package xyz.romakononovich.camera.di.activity

import dagger.Module
import dagger.Provides
import xyz.romakononovich.camera.presentation.gallery.GalleryActivity
import xyz.romakononovich.camera.presentation.router.RouterGallery
import xyz.romakononovich.camera.presentation.router.RouterGalleryImpl

/**
 * Created by RomanK on 11.05.18.
 */
@Module
class GalleryActivityModule {
    @Provides
    fun provideRouterGallery(activity: GalleryActivity): RouterGallery = RouterGalleryImpl(activity)
}