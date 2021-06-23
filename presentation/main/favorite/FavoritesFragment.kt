package com.lotteryadviser.presentation.main.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lotteryadviser.R
import com.lotteryadviser.databinding.FragmentFavoritesBinding
import com.lotteryadviser.di.Injector
import com.lotteryadviser.domain.model.Result
import com.lotteryadviser.domain.repository.countries.model.Country
import com.lotteryadviser.domain.usesase.FavoriteLotteriesUseCase
import com.lotteryadviser.presentation.base.BaseFragment
import com.lotteryadviser.presentation.main.TabIdentificator
import com.lotteryadviser.presentation.main.favorite.adapter.FavoriteLotteriesAdapter
import com.lotteryadviser.presentation.main.favorite.model.WrapperLotteryItem
import com.lotteryadviser.presentation.main.favorite.model.convertToUiModel
import com.lotteryadviser.utils.displayName
import com.lotteryadviser.views.ListViewExt
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavoritesFragment : BaseFragment<FavoritesViewModel>(), TabIdentificator {

    private var uiJob: Job? = null
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var adapter: FavoriteLotteriesAdapter

    override fun inject() = Injector.mainComponent().inject(this)

    override fun injectViewModel() {
        viewModel = getViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = FavoriteLotteriesAdapter(onActionsItemListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnBack.setOnClickListener { parentFragmentManager.popBackStack() }
        binding.btnRemoveSelectedItems.setOnClickListener {
            val removedItems = adapter.removeSelectedItems()
            viewModel.removeFromFavorites(removedItems)
            if (adapter.itemCount == 0) {
                binding.lotteries.state = ListViewExt.State.EMPTY
            }
        }

        binding.btnChangeCountry.setOnClickListener { viewModel.changeCountry() }

        binding.lotteries.list.layoutManager = LinearLayoutManager(requireContext())
        binding.lotteries.list.adapter = adapter
        binding.lotteries.setOnRefreshListener { viewModel.loadData() }
    }

    private fun bindingCurrentCountry(country: Country) {
        binding.btnChangeCountry.text = country.displayName(requireContext())
    }

    private fun bindLotteries(result: Result<FavoriteLotteriesUseCase.State>) {
        val items = result.data.lotteries
        adapter.swap(items.convertToUiModel())
        binding.lotteries.state = when (result) {
            is Result.Loading -> ListViewExt.State.PROGRESS
            is Result.Success -> {
                if (items.isEmpty()) {
                    binding.btnRemoveSelectedItems.isVisible = false
                    ListViewExt.State.EMPTY
                } else {
                    binding.btnRemoveSelectedItems.isVisible = true
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
        return R.id.nav_favorites
    }

    private val onActionsItemListener = object : FavoriteLotteriesAdapter.OnActionListener {

        override fun onItemClick(item: WrapperLotteryItem) {
            viewModel.forwardLottery(item)
        }

        override fun onItemSelected(item: WrapperLotteryItem) {

        }

        override fun onEditMode(active: Boolean) {
            binding.btnRemoveSelectedItems.visibility = if (active) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        }
    }
}