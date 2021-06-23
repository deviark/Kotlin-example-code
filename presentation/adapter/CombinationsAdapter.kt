package com.lotteryadviser.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.lotteryadviser.databinding.ItemCombinationNumbersBinding
import com.lotteryadviser.domain.repository.recommendcombinations.model.RecommendCombinationNumbers
import com.lotteryadviser.presentation.adapter.number.NumbersAdapter

class CombinationsAdapter :
    RecyclerView.Adapter<CombinationsAdapter.ViewHolder>() {

    private val items: MutableList<RecommendCombinationNumbers> = ArrayList()

    fun swap(items: List<RecommendCombinationNumbers>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCombinationNumbersBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, position + 1)
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(private val binding: ItemCombinationNumbersBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val adapter: NumbersAdapter = NumbersAdapter()

        init {
            val layoutManager = FlexboxLayoutManager(binding.root.context)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.flexWrap = FlexWrap.WRAP
            binding.numbers.layoutManager = layoutManager
            binding.numbers.adapter = adapter
        }

        fun bind(item: RecommendCombinationNumbers, position: Int) {
            binding.labelNumberCombinations.text = "$position"
            adapter.swap(item.numbers)
        }
    }

}