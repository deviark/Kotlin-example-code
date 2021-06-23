package com.lotteryadviser.presentation.dialog

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import com.lotteryadviser.databinding.FragmentCountCombinationBinding
import com.lotteryadviser.di.Injector
import com.lotteryadviser.presentation.base.BaseDialog
import com.lotteryadviser.presentation.base.StubViewModel
import kotlinx.parcelize.Parcelize

class CombinationOptionDialog : BaseDialog<StubViewModel>() {

    companion object {
        const val REQUEST_KEY = "count_combination_request_key"

        private const val TAG = "CountCombinationDialog"
        private const val MAX_COUNT_COMBINATION = 1_000
        private const val NOT_VALUE = -1
        private const val ARG_COUNT_COMBINATION = "arg_count_combination"
        private const val ARG_MIN_SIZE_COMBINATION = "arg_min_size_combination"
        private const val ARG_MAX_SIZE_COMBINATION = "arg_max_size_combination"


        fun show(minSizeCombination: Int?, maxSizeCombination: Int, fm: FragmentManager) {
            val args = Bundle()
            args.putInt(ARG_MIN_SIZE_COMBINATION, minSizeCombination ?: NOT_VALUE)
            args.putInt(ARG_MAX_SIZE_COMBINATION, maxSizeCombination)
            val dialog = CombinationOptionDialog()
            dialog.arguments = args
            fm.beginTransaction()
                .add(dialog, TAG)
                .commitAllowingStateLoss()
        }

        fun getCombinationsOption(data: Bundle): CombinationOption {
            return (data.getParcelable(ARG_COUNT_COMBINATION) as CombinationOption?)!!
        }

    }

    private lateinit var binding: FragmentCountCombinationBinding
    private var sizeCombination = NOT_VALUE

    override fun inject() = Injector.dialogComponent().inject(this)

    override fun injectViewModel() {
        viewModel = getViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCountCombinationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val minSizeCombination = requireArguments().getInt(ARG_MIN_SIZE_COMBINATION)
        if (minSizeCombination != NOT_VALUE) {
            val maxSizeCombination = requireArguments().getInt(ARG_MAX_SIZE_COMBINATION)
            sizeCombination = maxSizeCombination

            binding.labelMinSizeCombination.text = minSizeCombination.toString()
            binding.labelMaxSizeCombination.text = maxSizeCombination.toString()
            binding.valueSizeCombination.text = sizeCombination.toString()

            binding.formSizeCombination.visibility = View.VISIBLE
            binding.slideSizeCombination.value = sizeCombination.toFloat()
            binding.slideSizeCombination.valueFrom = minSizeCombination.toFloat()
            binding.slideSizeCombination.valueTo = maxSizeCombination.toFloat()
            binding.slideSizeCombination.stepSize = 1.0f
            binding.slideSizeCombination.addOnChangeListener { _, value, _ ->
                sizeCombination = value.toInt()
                binding.valueSizeCombination.text = sizeCombination.toString()
            }
        } else {
            binding.formSizeCombination.visibility = View.GONE
        }
        binding.btnClose.setOnClickListener { dismiss() }
        binding.btnApply.setOnClickListener {
            val value = binding.valueCountCombinations.text.toString()
            val countCombination = Integer.valueOf(value)
            val data = Bundle()
            val valueSizeCombination = if (sizeCombination == NOT_VALUE) {
                null
            } else {
                sizeCombination
            }
            data.putParcelable(
                ARG_COUNT_COMBINATION,
                CombinationOption(countCombination, valueSizeCombination)
            )
            setFragmentResult(REQUEST_KEY, data)
            dismiss()
        }

        binding.valueCountCombinations.addTextChangedListener { text ->
            updateApplyButton(text.toString())
        }

        updateApplyButton(binding.valueCountCombinations.text.toString())
    }


    private fun updateApplyButton(value: String) {
        val number = try {
            val valueNumber = Integer.valueOf(value)
            if (valueNumber <= MAX_COUNT_COMBINATION) {
                valueNumber
            } else {
                -1
            }
        } catch (e: Exception) {
            -1
        }
        binding.btnApply.isEnabled = value.isNotEmpty() && number > 0
    }

    @Parcelize
    data class CombinationOption(
        val countCombination: Int,
        val sizeCombination: Int?
    ) : Parcelable
}