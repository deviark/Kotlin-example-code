package com.lotteryadviser.presentation.base

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lotteryadviser.analytic.AnalyticManager
import javax.inject.Inject

abstract class BaseFragment<VM : BaseViewModel>: Fragment() {

    protected lateinit var viewModel: VM

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory



    override fun onAttach(context: Context) {
        inject()
        super.onAttach(context)
    }

    protected abstract fun inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    protected abstract fun injectViewModel()

    protected inline fun <reified T : ViewModel> getViewModel(): T {
        return ViewModelProvider(this, viewModelFactory)[T::class.java]
    }

}