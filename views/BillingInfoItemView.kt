package com.lotteryadviser.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.lotteryadviser.R
import com.lotteryadviser.databinding.ViewBillingInfoItemBinding

class BillingInfoItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewBillingInfoItemBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = ViewBillingInfoItemBinding.inflate(inflater, this)

        context.theme.obtainStyledAttributes(attrs, R.styleable.BillingInfoItemView, 0, 0 ).apply {
            try {
                binding.titlePeriod.text = getText(R.styleable.BillingInfoItemView_label_period)
            } finally {
                recycle()
            }
        }
    }

    fun setPrice(value: String) {
        binding.labelPrice.text = value
    }
}