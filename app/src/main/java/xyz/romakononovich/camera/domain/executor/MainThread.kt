package xyz.romakononovich.camera.domain.executor

/**
 * Created by RomanK on 05.05.18.
 */
interface MainThread {

    /**
     * Make runnable operation run in the main thread.
     *
     * @param runnable The runnable to run.
     */
    fun post(runnable: () -> Unit)

}