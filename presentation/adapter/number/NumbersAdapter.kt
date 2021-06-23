package com.lotteryadviser.presentation.adapter.number

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lotteryadviser.databinding.ItemBallNumberBinding
import com.lotteryadviser.domain.repository.gamesresults.model.NumberResult

class NumbersAdapter : RecyclerView.Adapter<NumbersAdapter.ViewHolder>() {

    private val items: MutableList<NumberResult> = ArrayList()

    fun swap(items: List<NumberResult>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBallNumberBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(private val binding: ItemBallNumberBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NumberResult) {
            binding.numberValue.setBackgroundResource(item.getBackgroundRes())
            binding.numberValue.text = "${item.value}"
            val color = ContextCompat.getColor(binding.numberValue.context, item.getColorRes())
            binding.numberValue.setTextColor(color)
        }
    }


}