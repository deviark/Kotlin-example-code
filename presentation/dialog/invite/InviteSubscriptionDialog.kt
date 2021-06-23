package com.lotteryadviser.presentation.dialog.invite

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.lotteryadviser.R
import com.lotteryadviser.di.Injector
import com.lotteryadviser.presentation.base.BaseDialog
import com.lotteryadviser.presentation.dialog.invite.reminder.PreferenceUtil

class InviteSubscriptionDialog : BaseDialog<InviteSubscriptionViewModel>() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setMessage(R.string.message_invite_subscription_dialog)
            .setNeutralButton(R.string.btn_later, dialogInterface)
            .setPositiveButton(R.string.btn_try, dialogInterface)
            .create()
    }

    private val dialogInterface = DialogInterface.OnClickListener { _, which ->
        when (which) {
            DialogInterface.BUTTON_POSITIVE ->
                viewModel.forwardSubscription()
            DialogInterface.BUTTON_NEUTRAL ->
                PreferenceUtil.onLaterButtonClicked(requireContext())

        }
        dismiss()
    }

    override fun inject() = Injector.dialogComponent().inject(this)

    override fun useDecor(): Boolean {
        return false
    }

    override fun injectViewModel() {
        viewModel = getViewModel()
    }
}