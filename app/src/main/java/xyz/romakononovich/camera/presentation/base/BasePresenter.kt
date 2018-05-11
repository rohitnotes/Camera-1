package xyz.romakononovich.camera.presentation.base

/**
 * Created by RomanK on 05.05.18.
 */
interface BasePresenter<V : BaseView> {

    fun onAttach(view: V)

    fun onDetach()

    fun view(): V?
}