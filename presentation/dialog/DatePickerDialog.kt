package com.lotteryadviser.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import com.lotteryadviser.databinding.FragmentDatePickerBinding
import com.lotteryadviser.di.Injector
import com.lotteryadviser.domain.utils.DateTimeUtils
import com.lotteryadviser.presentation.base.BaseDialog
import com.lotteryadviser.presentation.base.StubViewModel
import java.time.Instant

class DatePickerDialog : BaseDialog<StubViewModel>() {

    companion object {
        const val REQUEST_KEY = "date_picker_request_key"
        private const val ARG_SELECTED_DATE = "arg_selected_date"
        private const val ARG_MAX_DATE = "arg_max_date"
        private const val TAG = "DatePickerDialog"

        fun showDialog(
            selectedDate: Instant,
            maxDate: Instant,
            fm: FragmentManager,
        ) {
            val args = Bundle()
            args.putSerializable(ARG_SELECTED_DATE, selectedDate)
            args.putSerializable(ARG_MAX_DATE, maxDate)
            val dialog = DatePickerDialog()
            dialog.arguments = args
            fm.beginTransaction()
                .add(dialog, TAG)
                .commitAllowingStateLoss()
        }

        fun getSelectedDate(data: Bundle): Instant {
            return data.getSerializable(ARG_SELECTED_DATE) as Instant
        }
    }

    private lateinit var binding: FragmentDatePickerBinding

    private var selectedDate: Instant
        set(value) = requireArguments().putSerializable(ARG_SELECTED_DATE, value)
        get() = requireArguments().getSerializable(ARG_SELECTED_DATE) as Instant

    private val maxDate: Instant
        get() = requireArguments().getSerializable(ARG_MAX_DATE) as Instant

    override fun inject() = Injector.dialogComponent().inject(this)

    override fun injectViewModel() {
        viewModel = getViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDatePickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.calendarView.maxDate = maxDate.toEpochMilli()
        binding.calendarView.date = selectedDate.toEpochMilli()
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = DateTimeUtils.createDate(year, month + 1, dayOfMonth)
        }
        binding.btnApply.setOnClickListener {
            val data = Bundle()
            data.putSerializable(ARG_SELECTED_DATE, selectedDate)
            setFragmentResult(REQUEST_KEY, data)
            dismiss()
        }
    }


    override fun onDetach() {
        Injector.clearDialogComponent()
        super.onDetach()
    }

}