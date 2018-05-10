package xyz.romakononovich.camera.presentation.view

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import xyz.romakononovich.camera.R
import xyz.romakononovich.camera.utils.IMAGE_ID

/**
 * Created by RomanK on 10.05.18.
 */
class DeleteDialog : DialogFragment() {

    companion object {
        fun newInstance(id: Int) =
                DeleteDialog().apply {
                    arguments = Bundle(1).apply {
                        putInt(IMAGE_ID, id)
                    }
                }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val id: Int = arguments!!.getInt(IMAGE_ID)

        val dialogListener = activity

        return if (dialogListener is DeleteDialogListener) {
            AlertDialog.Builder(dialogListener)
                    .setMessage(R.string.dialog_message_delete)
                    .setPositiveButton(R.string.dialog_btn_delete, { _, _ ->
                        dialogListener.onDeleteDialogPositiveClick(id)
                    })
                    .setNegativeButton(R.string.dialog_dtn_cancel, { _, _ ->
                        dismiss()
                    })
                    .setCancelable(false)
                    .create()
        } else {
            super.onCreateDialog(savedInstanceState)
        }
    }

    interface DeleteDialogListener {
        fun onDeleteDialogPositiveClick(id: Int)
    }
}