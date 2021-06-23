package com.lotteryadviser.presentation.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.lotteryadviser.databinding.ItemLotteryBinding
import com.lotteryadviser.domain.repository.lotteries.model.Lottery
import com.lotteryadviser.utils.displayJackpot
import com.lotteryadviser.utils.loadCircularImage

class LotteriesAdapter(
    private val listener: OnActionListener,
    private val showJackpot: Boolean = true
) : RecyclerView.Adapter<LotteriesAdapter.ViewHolder>() {


    interface OnActionListener {
        fun onItemClick(item: Lottery)
    }

    private val items: MutableList<Lottery> = ArrayList()

    fun swap(items: List<Lottery>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLotteryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ItemLotteryBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(item: Lottery) {
            binding.lotteryJackPot.isVisible = showJackpot
            binding.lotteryJackPot.text = item.displayJackpot()
            binding.lotteryName.text = item.name
            val colorBorder = Color.parseColor("#92DF49")
            binding.lotteryImage.loadCircularImage(item.imageUrl, 6.0f, colorBorder)
        }

        override fun onClick(v: View?) {
            listener.onItemClick(items[adapterPosition])
        }
    }
}