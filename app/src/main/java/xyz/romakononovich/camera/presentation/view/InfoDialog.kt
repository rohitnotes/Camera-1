package xyz.romakononovich.camera.presentation.view

import android.app.Dialog
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.os.Bundle
import xyz.romakononovich.camera.R
import xyz.romakononovich.camera.utils.SOURCE_DIALOG

/**
 * Created by RomanK on 13.05.18.
 */
class InfoDialog : DialogFragment() {
    companion object {
        fun newInstance(source: String) =
                InfoDialog().apply {
                    arguments = Bundle(1).apply {
                        putString(SOURCE_DIALOG, source)
                    }
                }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val source = arguments?.getString(SOURCE_DIALOG)

        return AlertDialog.Builder(activity!!)
                .setTitle(R.string.dialog_info_title)
                .setMessage(source)
                .setNegativeButton(R.string.dialog_btn_close, { _, _ ->
                    dismiss()
                })
                .setCancelable(false)
                .create()
    }
}