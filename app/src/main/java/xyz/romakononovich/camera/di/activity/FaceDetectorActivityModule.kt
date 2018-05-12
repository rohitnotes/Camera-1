package xyz.romakononovich.camera.di.activity

import dagger.Module
import dagger.Provides
import xyz.romakononovich.camera.presentation.facedetect.FaceDetectActivity
import xyz.romakononovich.camera.presentation.router.Router
import xyz.romakononovich.camera.presentation.router.RouterImpl

/**
 * Created by RomanK on 12.05.18.
 */

@Module
class FaceDetectorActivityModule {
    @Provides
    fun provideRouter(activity: FaceDetectActivity): Router = RouterImpl(activity)


}

