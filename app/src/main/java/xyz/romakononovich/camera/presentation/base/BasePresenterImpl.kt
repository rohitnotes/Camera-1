package xyz.romakononovich.camera.presentation.base

/**
 * Created by RomanK on 11.05.18.
 */
open class BasePresenterImpl<V : BaseView> : BasePresenter<V> {
    private var view: V? = null

    override fun onAttach(view: V) {
        this.view = view
    }

    override fun onDetach() {
        view = null
    }

    override fun view(): V? {
        return view
    }
}