package com.lotteryadviser.presentation

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.google.android.gms.ads.MobileAds
import com.lotteryadviser.BuildConfig
import com.lotteryadviser.R
import com.lotteryadviser.databinding.ActivityMainBinding
import com.lotteryadviser.di.Injector
import com.lotteryadviser.presentation.base.BaseActivity
import com.lotteryadviser.presentation.dialog.InfoDialog
import com.lotteryadviser.presentation.dialog.invite.reminder.InviteSubscription
import com.suddenh4x.ratingdialog.AppRating
import com.suddenh4x.ratingdialog.preferences.MailSettings
import com.suddenh4x.ratingdialog.preferences.RatingThreshold
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SingleActivity : BaseActivity<SingleActivityViewModel>(), InfoDialog.InfoDialogListener {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    private val navigator = AppNavigator(this, R.id.root_content)

    private var uiJob: Job? = null

    private var showedRatingDialog: Boolean = false
    private var createInviteSubscription = false

    override fun injectActivity() = Injector.activityComponent().inject(this)

    override fun injectViewModel() {
        viewModel = getViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_LotteryAdviser)
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {}
        binding = ActivityMainBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
        binding.progressBar.show()
        lifecycleScope.launchWhenCreated {
            viewModel.hasErrorUpdate.collect { value ->
                if (value == true) {
                    InfoDialog.show(
                        R.string.title_error,
                        R.string.message_error_update_data,
                        supportFragmentManager
                    )
                }
            }
        }

        if (savedInstanceState == null) {
            val mailSettings = MailSettings(
                getString(R.string.support_email),
                getString(R.string.mail_subject),
                "",
                getString(R.string.mail_error)
            )
            showedRatingDialog = AppRating.Builder(this)
                .setMinimumLaunchTimes(5)
                .setMinimumDays(BuildConfig.RATING_DIALOG_MINUM_DAYS)
                .setMinimumLaunchTimesToShowAgain(5)
                .setMinimumDaysToShowAgain(BuildConfig.RATING_DIALOG_MINUM_DAYS)
                .setRatingThreshold(RatingThreshold.FOUR)
                .setMailSettingsForFeedbackDialog(mailSettings)
                .showIfMeetsConditions()
        }
    }

    override fun onStart() {
        super.onStart()
        uiJob = lifecycleScope.launch {
            viewModel.hasSubscription.collect { value ->
                if (value == false && !createInviteSubscription && !showedRatingDialog) {
                    createInviteSubscription = true
                    InviteSubscription.Builder(this@SingleActivity)
                        .setMinimumLaunchTimes(3)
                        .setMinimumDays(BuildConfig.RATING_DIALOG_MINUM_DAYS)
                        .setMinimumLaunchTimesToShowAgain(6)
                        .setMinimumDaysToShowAgain(BuildConfig.RATING_DIALOG_MINUM_DAYS_SHOW_AGAIN)
                        .showIfMeetsConditions()
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        uiJob?.cancel()
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onClickPositiveButton() {
        viewModel.updateData()
    }

    override fun onClickNegativeButton() {
        finish()
    }

}