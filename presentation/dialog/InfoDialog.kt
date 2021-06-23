package com.lotteryadviser.presentation.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

class InfoDialog: DialogFragment() {

    companion object {
        private const val ARG_TITTLE = "arg_tittle"
        private const val ARG_MESSAGE = "arg_message"

        fun show(
            @StringRes tittle: Int,
            @StringRes message: Int,
            fm: FragmentManager
        ) {
            val args = Bundle()
            args.putInt(ARG_TITTLE, tittle)
            args.putInt(ARG_MESSAGE, message)
            val dialog = InfoDialog()
            dialog.arguments = args
            fm.beginTransaction()
                .add(dialog, "")
                .commitAllowingStateLoss()
        }
    }

    interface InfoDialogListener {
        fun onClickPositiveButton()
        fun onClickNegativeButton()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = requireArguments().getInt(ARG_TITTLE)
        val message = requireArguments().getInt(ARG_MESSAGE)
        return AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, dialogInterface)
            .setNegativeButton(android.R.string.cancel, dialogInterface)
            .create()
    }


    private val dialogInterface = DialogInterface.OnClickListener { _, which ->
         when(which) {
             DialogInterface.BUTTON_POSITIVE -> onClickPositiveButton()
             DialogInterface.BUTTON_NEGATIVE -> onClickNegativeButton()
         }
        dismiss()
    }

    private fun onClickPositiveButton() {
        val activity = requireActivity()
        if (activity is InfoDialogListener) {
            activity.onClickPositiveButton()
        }
    }

    private fun onClickNegativeButton() {
        val activity = requireActivity()
        if (activity is InfoDialogListener) {
            activity.onClickNegativeButton()
        }
    }
}