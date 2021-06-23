package com.lotteryadviser.presentation.language.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lotteryadviser.databinding.ItemLanguageBinding
import com.lotteryadviser.presentation.language.model.WrapperLanguage

class LanguagesAdapter(private val onActionListener: OnActionListener) :
    RecyclerView.Adapter<LanguagesAdapter.ViewHolder>() {

    interface OnActionListener {
        fun onSelectedItem(item: WrapperLanguage)
    }

    private var selectedIndex = -1

    private val items: MutableList<WrapperLanguage> = ArrayList()

    fun swap(items: List<WrapperLanguage>) {
        this.items.clear()
        this.items.addAll(items)
        for (i in 0..items.lastIndex) {
            if (items[i].selected) {
                selectedIndex = i
                break
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLanguageBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private lateinit var item: WrapperLanguage

        fun bind(item: WrapperLanguage) {
            binding.root.setOnClickListener(this)
            this.item = item
            binding.languageName.setText(item.language.label)
            binding.languageName.isSelected = item.selected
        }

        override fun onClick(v: View?) {
            if (selectedIndex != adapterPosition) {
                items[selectedIndex].selected = false
                notifyItemChanged(selectedIndex)
                item.selected = true
                selectedIndex = adapterPosition
                notifyItemChanged(selectedIndex)
                onActionListener.onSelectedItem(item)
            }
        }
    }
}