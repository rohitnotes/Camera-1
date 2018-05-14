package xyz.romakononovich.camera.presentation.view

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import xyz.romakononovich.camera.R
import xyz.romakononovich.camera.utils.QR_CODE

/**
 * Created by RomanK on 10.05.18.
 */
class QrCodeDialog : DialogFragment() {
    companion object {
        fun newInstance(it: String) =
                QrCodeDialog().apply {
                    arguments = Bundle(1).apply {
                        putString(QR_CODE, it)
                    }
                }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val it: String = arguments!!.getString(QR_CODE)

        val dlg = AlertDialog.Builder(requireContext())
                .setMessage(it)
                .setNegativeButton(R.string.dialog_btn_close, { _, _ ->
                    dismiss()
                })
                .setCancelable(false)
        if (it.startsWith("http://") || it.startsWith("https://")) {
            dlg.setPositiveButton(getString(R.string.dialog_btn_open_web), { _, _ ->
                CustomTabsIntent.Builder().build().launchUrl(activity, Uri.parse(it))
            })
        }
        return dlg.create()


    }
}