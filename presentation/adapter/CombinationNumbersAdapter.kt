package com.lotteryadviser.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.lotteryadviser.databinding.ItemCombinationBinding
import com.lotteryadviser.domain.repository.combinations.model.CombinationNumbers
import com.lotteryadviser.utils.displayName
import com.lotteryadviser.utils.getDrawGameDisplay
import com.lotteryadviser.utils.getGameSystem

class CombinationNumbersAdapter(
    private val listener: OnActionListener,
    private val showNameLottery: Boolean = true
) :
    RecyclerView.Adapter<CombinationNumbersAdapter.ViewHolder>() {

    interface OnActionListener {
        fun onItemClick(item: CombinationNumbers)
    }

    private val items: MutableList<CombinationNumbers> = ArrayList()


    fun swap(items: List<CombinationNumbers>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCombinationBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ItemCombinationBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(item: CombinationNumbers) {
            val context = binding.root.context
            binding.nameSystem.text = item.getGameSystem().displayName(context)

            binding.labelSystemSizeCombination.isVisible = item.showSizeCombination
            binding.valueSystemSizeCombination.isVisible = item.showSizeCombination
            binding.valueSystemSizeCombination.text = "${item.systemSizeCombination}"

            binding.nameLottery.text = if (showNameLottery) {
                item.lotteryName
            } else {
                ""
            }
            binding.valueNumberGame.text = item.getDrawGameDisplay()
            binding.valueCountCombinations.text = "${item.countCombinations}"
        }

        override fun onClick(v: View?) {
            val item = items[adapterPosition]
            listener.onItemClick(item)
        }
    }


}