package com.lotteryadviser.presentation.country.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lotteryadviser.databinding.ItemCountryBinding
import com.lotteryadviser.domain.repository.countries.model.Country
import com.lotteryadviser.utils.displayName

class CountriesAdapter() :
    RecyclerView.Adapter<CountriesAdapter.ViewHolder>() {


    private var selectedIndex = -1

    private val items: MutableList<Country> = ArrayList()


    fun getSelectedCountry(): Country {
        return items[selectedIndex]
    }

    fun swap(items: List<Country>) {
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
        val binding = ItemCountryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ItemCountryBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private lateinit var item: Country

        fun bind(item: Country) {
            binding.root.setOnClickListener(this)
            this.item = item
            binding.countryName.text = item.displayName(binding.countryName.context)
            binding.countryName.isSelected = item.selected
        }

        override fun onClick(v: View?) {
            if (selectedIndex != adapterPosition) {
                items[selectedIndex].selected = false
                notifyItemChanged(selectedIndex)
                item.selected = true
                selectedIndex = adapterPosition
                notifyItemChanged(selectedIndex)
            }
        }
    }
}