package xyz.romakononovich.camera.di.activity

import dagger.Module
import dagger.Provides
import xyz.romakononovich.camera.presentation.main.MainActivity
import xyz.romakononovich.camera.presentation.router.Router
import xyz.romakononovich.camera.presentation.router.RouterImpl

/**
 * Created by RomanK on 11.05.18.
 */

@Module
class MainActivityModule {
    @Provides
    fun provideRouterMain(activity: MainActivity): Router = RouterImpl(activity)
}