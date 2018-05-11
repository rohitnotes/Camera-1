package xyz.romakononovich.camera

import android.app.Activity
import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import xyz.romakononovich.camera.di.application.DaggerAppComponent
import javax.inject.Inject

/**
 * Created by RomanK on 11.05.18.
 */
class App : Application(),
        HasActivityInjector {
    override fun activityInjector(): AndroidInjector<Activity> {
        return activityInjector
    }

    @Inject lateinit var activityInjector: DispatchingAndroidInjector<Activity>


    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder().application(this).build().inject(this)
    }

}