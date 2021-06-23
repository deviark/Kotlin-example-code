package com.lotteryadviser.presentation.country

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lotteryadviser.databinding.FragmentCountriesBinding
import com.lotteryadviser.di.Injector
import com.lotteryadviser.presentation.base.BaseFragment
import com.lotteryadviser.presentation.country.adapter.CountriesAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CountriesFragment : BaseFragment<CountriesViewModel>() {

    companion object {
        private const val ARG_FIRST_LAUNCH = "first_launch"

        fun newInstance(firstLaunch: Boolean): CountriesFragment {
            val args = Bundle()
            args.putBoolean(ARG_FIRST_LAUNCH, firstLaunch)
            val fragment = CountriesFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var uiJob: Job? = null

    private lateinit var binding: FragmentCountriesBinding

    private lateinit var adapter: CountriesAdapter

    override fun inject() = Injector.countriesComponent().inject(this)

    override fun injectViewModel() {
        viewModel = getViewModel()
        val firstLaunch = requireArguments().getBoolean(ARG_FIRST_LAUNCH, false)
        viewModel.init(firstLaunch)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = CountriesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCountriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.countriesList.layoutManager = LinearLayoutManager(requireContext())
        binding.countriesList.adapter = adapter

        binding.btnSaveCountry.setOnClickListener {
            viewModel.changeCountry(adapter.getSelectedCountry())
        }
    }

    override fun onStart() {
        super.onStart()
        uiJob = lifecycleScope.launch {
            viewModel.countries.collect { items ->
                adapter.swap(items)
            }
        }
    }

    override fun onStop() {
        uiJob?.cancel()
        super.onStop()
    }


}