package xyz.romakononovich.camera.di.application

import android.app.Application
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import xyz.romakononovich.camera.App
import xyz.romakononovich.camera.di.activity.ActivityBuilder
import xyz.romakononovich.camera.di.activity.GalleryActivityModule
import xyz.romakononovich.camera.di.activity.MainActivityModule
import javax.inject.Singleton

/**
 * Created by RomanK on 11.05.18.
 */
@Singleton
@Component(modules =
    [AppModule::class,
    ActivityBuilder::class,
    MainActivityModule::class,
    GalleryActivityModule::class])
interface AppComponent : AndroidInjector<Application> {
    fun inject(app: App)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun application(context: Context): Builder

    }
}