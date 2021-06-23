package com.lotteryadviser.presentation.main.combinations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lotteryadviser.R
import com.lotteryadviser.databinding.FragmentCombinationsBinding
import com.lotteryadviser.di.Injector
import com.lotteryadviser.domain.model.Filter
import com.lotteryadviser.domain.model.Result
import com.lotteryadviser.domain.repository.combinations.model.CombinationNumbers
import com.lotteryadviser.domain.usesase.CombinationsUseCase
import com.lotteryadviser.domain.utils.DateTimeUtils
import com.lotteryadviser.presentation.adapter.CombinationNumbersAdapter
import com.lotteryadviser.presentation.base.BaseFragment
import com.lotteryadviser.presentation.dialog.DatePickerDialog
import com.lotteryadviser.presentation.main.TabIdentificator
import com.lotteryadviser.utils.displayName
import com.lotteryadviser.views.ListViewExt
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class CombinationsFragment : BaseFragment<CombinationsViewModel>(), TabIdentificator {

    private var uiJob: Job? = null
    private lateinit var binding: FragmentCombinationsBinding
    private lateinit var adapter: CombinationNumbersAdapter

    override fun inject() = Injector.mainComponent().inject(this)

    override fun injectViewModel() {
        viewModel = getViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = CombinationNumbersAdapter(onActionItemListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCombinationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnBack.setOnClickListener { parentFragmentManager.popBackStack() }

        binding.btnCalendar.setOnClickListener {
            DatePickerDialog.showDialog(
                viewModel.selectedDate(),
                viewModel.maxDate(),
                parentFragmentManager
            )
        }
        setFragmentResultListener(DatePickerDialog.REQUEST_KEY) { _, data ->
            val date = DatePickerDialog.getSelectedDate(data)
            viewModel.changeDate(date)
        }

        binding.btnChangeCountry.setOnClickListener { viewModel.changeCountry() }

        binding.combinations.list.layoutManager = LinearLayoutManager(requireContext())
        binding.combinations.list.adapter = adapter
        binding.combinations.setOnRefreshListener { viewModel.loadData() }
    }

    private fun bindFilter(filter: Filter) {
        binding.btnChangeCountry.text = filter.country.displayName(requireContext())
        binding.valueSelectedDate.text = DateTimeUtils.covertTimestampToString(filter.date)
    }

    private fun bindCombinations(result: Result<CombinationsUseCase.State>) {
        val items = result.data.combinations
        adapter.swap(items)
        binding.combinations.state = when (result) {
            is Result.Loading -> ListViewExt.State.PROGRESS
            is Result.Success -> {
                if (items.isEmpty()) {
                    ListViewExt.State.EMPTY
                } else {
                    ListViewExt.State.CONTENT
                }
            }
            is Result.Failure -> ListViewExt.State.ERROR
        }
    }

    override fun onStart() {
        super.onStart()
        uiJob = lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if (state != null) {
                    bindFilter(state.data.filter)
                    bindCombinations(state)
                }
            }
        }
    }

    override fun onStop() {
        uiJob?.cancel()
        super.onStop()
    }

    override fun getTabId(): Int {
        return R.id.nav_combinations_history
    }

    private val onActionItemListener = object : CombinationNumbersAdapter.OnActionListener {
        override fun onItemClick(item: CombinationNumbers) {
            viewModel.forwardToHistoryItem(item)
        }
    }

}