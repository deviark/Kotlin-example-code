package com.lotteryadviser.presentation.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdRequest
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lotteryadviser.R
import com.lotteryadviser.databinding.FragmentMainBinding
import com.lotteryadviser.di.Injector
import com.lotteryadviser.presentation.base.BaseFragment
import com.lotteryadviser.presentation.dialog.invite.InviteSubscriptionDialog
import com.lotteryadviser.presentation.main.combinations.CombinationsFragment
import com.lotteryadviser.presentation.main.favorite.FavoritesFragment
import com.lotteryadviser.presentation.main.lotteries.LotteriesFragment
import com.lotteryadviser.presentation.main.lotteryresults.LotteriesResultsFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainFragment : BaseFragment<MainViewModel>() {

    private lateinit var binding: FragmentMainBinding
    private var uiJob: Job? = null
    private var addRequest: AdRequest? = null

    override fun inject() = Injector.mainComponent().inject(this)

    override fun injectViewModel() {
        viewModel = getViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.navigationView.setOnNavigationItemSelectedListener(navigationItemSelection)
        if (childFragmentManager.backStackEntryCount == 0) {
            binding.navigationView.selectedItemId = R.id.nav_lotteries
            binding.navigationView.performClick()
        }
        binding.navigationView.setOnNavigationItemReselectedListener { }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, true) {
            if (childFragmentManager.backStackEntryCount > 1) {
                childFragmentManager.popBackStackImmediate()
            } else {
                requireActivity().finish()
            }
        }

        childFragmentManager.addOnBackStackChangedListener(onBackStackChangedListener)
    }

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()
        uiJob = lifecycleScope.launch {
            viewModel.stateSubscription.collect { isActive ->
                if (!isActive) {
                    binding.adView.visibility = View.VISIBLE
                    binding.adView.loadAd(createAdRequest())
                } else {
                    binding.adView.visibility = View.GONE
                }
            }
        }
    }

    override fun onStop() {
        uiJob?.cancel()
        super.onStop()
    }

    private fun createAdRequest(): AdRequest {
        if (addRequest == null) {
            addRequest = AdRequest.Builder()
                .build()
        }
        return addRequest!!
    }


    override fun onDestroyView() {
        childFragmentManager.removeOnBackStackChangedListener(onBackStackChangedListener)
        super.onDestroyView()
    }

    private val onBackStackChangedListener = FragmentManager.OnBackStackChangedListener {
        val currentFragment = childFragmentManager.findFragmentById(R.id.frame_content)
        if (currentFragment is TabIdentificator) {
            binding.navigationView.setOnNavigationItemSelectedListener(null)
            binding.navigationView.selectedItemId = currentFragment.getTabId()
            binding.navigationView.setOnNavigationItemSelectedListener(navigationItemSelection)
        }
    }

    private val navigationItemSelection =
        BottomNavigationView.OnNavigationItemSelectedListener { itemMenu ->
            if (childFragmentManager.backStackEntryCount > 1) {
                childFragmentManager.popBackStack()
            }
            when (itemMenu.itemId) {
                R.id.nav_lotteries -> {
                    if (childFragmentManager.backStackEntryCount == 0) {
                        replaceFragment(LotteriesFragment())
                    }
                }
                R.id.nav_favorites -> {
                    replaceFragment(FavoritesFragment())
                }
                R.id.nav_combinations_history -> {
                    replaceFragment(CombinationsFragment())
                }
                R.id.nav_game_results -> {
                    replaceFragment(LotteriesResultsFragment())
                }
                else -> error("Unknown navigate itemID")
            }
            true
        }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.frame_content, fragment)
            .addToBackStack(null)
            .commit()
    }

}