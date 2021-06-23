package com.lotteryadviser.presentation.dialog.invite.reminder

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.lotteryadviser.presentation.dialog.invite.InviteSubscriptionDialog
import java.util.*
import java.util.concurrent.TimeUnit


object InviteSubscription {

    data class Builder(var activity: AppCompatActivity) {

        companion object {
            private const val TAG = "InviteSubscriptionDialog"
        }

        fun setMinimumLaunchTimes(launchTimes: Int) = apply {
            PreferenceUtil.setMinimumLaunchTimes(activity, launchTimes)
        }

        fun setMinimumLaunchTimesToShowAgain(launchTimesToShowAgain: Int) = apply {
            PreferenceUtil.setMinimumLaunchTimesToShowAgain(activity, launchTimesToShowAgain)
        }

        fun setMinimumDays(minimumDays: Int) = apply {
            PreferenceUtil.setMinimumDays(activity, minimumDays)
        }

        fun setMinimumDaysToShowAgain(minimumDaysToShowAgain: Int) = apply {
            PreferenceUtil.setMinimumDaysToShowAgain(activity, minimumDaysToShowAgain)
        }

        fun showIfMeetsConditions(): Boolean {
            return when {
                activity.supportFragmentManager.findFragmentByTag(TAG) != null -> {
                    false
                }
                shouldShowDialog(activity) -> {
                    showNow()
                    PreferenceUtil.increaseLaunchTimes(activity)
                    true
                }
                else -> {
                    PreferenceUtil.increaseLaunchTimes(activity)
                    false
                }
            }
        }

        fun showNow() {
            val dialog = InviteSubscriptionDialog()
            dialog.isCancelable = false
            activity.supportFragmentManager
                .beginTransaction()
                .add(dialog, TAG)
                .commitAllowingStateLoss()
        }

        private fun shouldShowDialog(context: Context): Boolean {
            val remindTimestamp = PreferenceUtil.getRemindTimestamp(context)
            val wasLaterButtonClicked = PreferenceUtil.wasLaterButtonClicked(context)
            val currentTimestamp = System.currentTimeMillis()
            val daysBetween = calculateDaysBetween(
                Date(remindTimestamp),
                Date(currentTimestamp)
            )

            return if (wasLaterButtonClicked) {
                daysBetween >= PreferenceUtil.getMinimumDaysToShowAgain(context) &&
                        (PreferenceUtil.getLaunchTimes(context) >=
                                PreferenceUtil.getMinimumLaunchTimesToShowAgain(context))
            } else {
                daysBetween >= PreferenceUtil.getMinimumDays(context) &&
                        PreferenceUtil.getLaunchTimes(context) >=
                        PreferenceUtil.getMinimumLaunchTimes(context)
            }
        }

        private fun calculateDaysBetween(d1: Date, d2: Date): Long {
            return TimeUnit.MILLISECONDS.toDays(d2.time - d1.time)
        }
    }
}