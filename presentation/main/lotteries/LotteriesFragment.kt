package com.lotteryadviser.presentation.main.lotteries

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lotteryadviser.R
import com.lotteryadviser.data.language.Languages
import com.lotteryadviser.databinding.FragmentLotteriesBinding
import com.lotteryadviser.di.Injector
import com.lotteryadviser.domain.model.Result
import com.lotteryadviser.domain.repository.countries.model.Country
import com.lotteryadviser.domain.repository.lotteries.model.Lottery
import com.lotteryadviser.domain.usesase.LotteriesUseCase
import com.lotteryadviser.presentation.adapter.LotteriesAdapter
import com.lotteryadviser.presentation.base.BaseFragment
import com.lotteryadviser.presentation.main.TabIdentificator
import com.lotteryadviser.utils.displayName
import com.lotteryadviser.views.ListViewExt
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LotteriesFragment : BaseFragment<LotteriesViewModel>(), TabIdentificator {

    private var uiJob: Job? = null
    private lateinit var binding: FragmentLotteriesBinding
    private lateinit var adapter: LotteriesAdapter

    override fun inject() = Injector.mainComponent().inject(this)

    override fun injectViewModel() {
        viewModel = getViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = LotteriesAdapter(onLotteryItemsListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLotteriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.toggleTheme.setOnClickListener { viewModel.toggleThemeLightDark() }

        binding.btnChangeLanguage.setOnClickListener { viewModel.changeLanguage() }

        binding.btnChangeCountry.setOnClickListener { viewModel.changeCountry() }

        binding.lotteries.list.layoutManager = LinearLayoutManager(requireContext())
        binding.lotteries.list.adapter = adapter
        binding.lotteries.setOnRefreshListener { viewModel.loadData() }

        binding.btnChangeLanguage.apply {
            paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
        }
    }

    private fun bindLanguage(language: Languages) {
        binding.labelSelectedLanguage.setText(language.label)
    }

    private fun bindingCurrentCountry(country: Country) {
        binding.btnChangeCountry.text = country.displayName(requireContext())
    }

    private fun bindLotteries(result: Result<LotteriesUseCase.State>) {
        val items = result.data.lotteries
        adapter.swap(items)
        binding.lotteries.state = when(result) {
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
            viewModel.usState.collect { state ->
                if (state != null ) {
                    bindLanguage(state.data.language)
                    bindingCurrentCountry(state.data.country)
                    bindLotteries(state)
                }
            }
        }
    }

    override fun onStop() {
        uiJob?.cancel()
        super.onStop()
    }

    override fun getTabId(): Int {
        return R.id.nav_lotteries
    }

    private val onLotteryItemsListener = object : LotteriesAdapter.OnActionListener {

        override fun onItemClick(item: Lottery) {
            viewModel.forwardToLottery(item)
        }

    }
}