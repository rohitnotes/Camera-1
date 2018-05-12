package xyz.romakononovich.camera.di.application

import android.app.Application
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import xyz.romakononovich.camera.App
import xyz.romakononovich.camera.di.activity.ActivityBuilder
import javax.inject.Singleton

/**
 * Created by RomanK on 11.05.18.
 */
@Singleton
@Component(modules =
[AndroidInjectionModule::class,
    AppModule::class,
    ActivityBuilder::class])
interface AppComponent : AndroidInjector<Application> {
    fun inject(app: App)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun application(context: Context): Builder

    }
}