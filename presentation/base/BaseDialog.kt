package com.lotteryadviser.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lotteryadviser.R
import javax.inject.Inject

abstract class BaseDialog<VM: BaseViewModel>: DialogFragment()   {

    protected lateinit var viewModel: VM

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun onAttach(context: Context) {
        inject()
        super.onAttach(context)
    }

    protected abstract fun inject()

    protected abstract fun injectViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectViewModel()
    }

    protected open fun useDecor(): Boolean {
        return true
    }

    override fun onStart() {
        super.onStart()
        if (useDecor()) {
            dialog!!.window!!.decorView.setBackgroundResource(R.color.transparent)
            dialog!!.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    protected inline fun <reified T : ViewModel> getViewModel(): T {
        return ViewModelProvider(this, viewModelFactory)[T::class.java]
    }

}