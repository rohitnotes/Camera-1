package xyz.romakononovich.camera.di.activity

import dagger.Module
import dagger.Provides
import xyz.romakononovich.camera.data.api.CameraApiImpl
import xyz.romakononovich.camera.domain.api.CameraApi
import xyz.romakononovich.camera.presentation.main.MainActivity
import xyz.romakononovich.camera.presentation.router.Router
import xyz.romakononovich.camera.presentation.router.RouterImpl
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by RomanK on 11.05.18.
 */

@Module
class MainActivityModule {
    @Provides
    fun provideRouter(activity: MainActivity): Router = RouterImpl(activity)
}