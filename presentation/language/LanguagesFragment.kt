package com.lotteryadviser.presentation.language

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lotteryadviser.databinding.FragmentLanguagesBinding
import com.lotteryadviser.di.Injector
import com.lotteryadviser.presentation.base.BaseFragment
import com.lotteryadviser.presentation.language.adapter.LanguagesAdapter
import com.lotteryadviser.presentation.language.model.WrapperLanguage
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LanguagesFragment : BaseFragment<LanguagesViewModel>() {

    private lateinit var binding: FragmentLanguagesBinding
    private lateinit var adpater: LanguagesAdapter

    private var uiJob: Job? = null

    override fun inject() = Injector.languagesComponent().inject(this)

    override fun injectViewModel() {
        viewModel = getViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adpater = LanguagesAdapter(onActionItemListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLanguagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.languagesList.layoutManager = LinearLayoutManager(requireContext())
        binding.languagesList.adapter = adpater

        binding.btnSaveLanguage.setOnClickListener { viewModel.saveLanguage() }

    }

    override fun onStart() {
        super.onStart()
        uiJob = lifecycleScope.launch {
            viewModel.languages.collect { languages ->
                val languagesSorted = languages.sortedBy { getString(it.language.label) }
                adpater.swap(languagesSorted)
            }
        }
    }

    override fun onStop() {
        uiJob?.cancel()
        super.onStop()
    }


    private val onActionItemListener = object : LanguagesAdapter.OnActionListener {
        override fun onSelectedItem(item: WrapperLanguage) {
            viewModel.selectedLanguage(item.language)
        }
    }
}