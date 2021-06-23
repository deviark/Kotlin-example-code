package com.lotteryadviser.presentation.main.favorite.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lotteryadviser.R
import com.lotteryadviser.databinding.ItemLotteryFavoriteBinding
import com.lotteryadviser.presentation.main.favorite.model.WrapperLotteryItem
import com.lotteryadviser.utils.displayJackpot
import com.lotteryadviser.utils.loadCircularImage

class FavoriteLotteriesAdapter(private val listener: OnActionListener) :
    RecyclerView.Adapter<FavoriteLotteriesAdapter.ViewHolder>() {

    interface OnActionListener {
        fun onItemClick(item: WrapperLotteryItem)
        fun onItemSelected(item: WrapperLotteryItem)
        fun onEditMode(active: Boolean)
    }

    private val items: MutableList<WrapperLotteryItem> = ArrayList()

    fun swap(items: List<WrapperLotteryItem>) {
        this.items.clear()
        this.items.addAll(items)
        updateEditMode()
        notifyDataSetChanged()
    }

    fun removeSelectedItems(): List<WrapperLotteryItem> {
        val selectedItems = items.filter { it.selected }
        val unselectedItems = items.filter { !it.selected }
        this.items.clear()
        this.items.addAll(unselectedItems)
        notifyDataSetChanged()
        updateEditMode()
        return selectedItems
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        val binding = ItemLotteryFavoriteBinding.inflate(inflate, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    private fun editMode(): Boolean {
        return items.find { it.selected } != null
    }

    private fun updateEditMode() {
        val editMode = editMode()
        listener.onEditMode(editMode)
    }

    inner class ViewHolder(private val binding: ItemLotteryFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private val colorBorder = Color.parseColor("#92DF49")

        init {
            binding.root.setOnClickListener(this)
            binding.btnSelect.setOnClickListener(this)
        }

        fun bind(item: WrapperLotteryItem) {
            binding.lotteryJackPot.text = item.lottery.displayJackpot()
            binding.lotteryName.text = item.lottery.name
            binding.btnSelect.isSelected = item.selected
            binding.lotteryImage.loadCircularImage(item.lottery.imageUrl, 12.0f, colorBorder)
        }

        override fun onClick(v: View) {
            val item = items[adapterPosition]
            when (v.id) {
                R.id.item_lottery -> {
                    listener.onItemClick(item)
                }
                R.id.btn_select -> {
                    item.selected = !item.selected
                    notifyItemChanged(adapterPosition)
                    updateEditMode()
                }
            }
        }
    }


}