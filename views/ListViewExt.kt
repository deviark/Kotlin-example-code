package com.lotteryadviser.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.lotteryadviser.R
import com.lotteryadviser.databinding.ViewListExtBinding

class ListViewExt @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var state: State = State.UNKNOWN
        set(value) {
            field = value
            when (state) {
                State.UNKNOWN -> {
                    binding.list.isVisible = false
                    binding.imageEmptyContent.isVisible = false
                    binding.labelEmptyContent.isVisible = false
                    binding.btnRefresh.isVisible = false
                    binding.progressBar.hide()
                    binding.swipeRefresh.isEnabled = false
                }
                State.PROGRESS -> {
                    if (!binding.swipeRefresh.isRefreshing) {
                        binding.list.isVisible = false
                        binding.imageEmptyContent.isInvisible = true
                        binding.labelEmptyContent.isInvisible = true
                        binding.progressBar.show()
                        binding.swipeRefresh.isEnabled = false
                    }
                    binding.btnRefresh.visibility = View.GONE
                }
                State.CONTENT -> {
                    binding.list.isVisible = true
                    binding.imageEmptyContent.isVisible = false
                    binding.labelEmptyContent.isVisible = false
                    binding.btnRefresh.isVisible = false
                    binding.progressBar.hide()
                    binding.swipeRefresh.isRefreshing = false
                    binding.swipeRefresh.isEnabled = enableSwipeRefresh
                }
                State.EMPTY -> {
                    binding.list.isVisible = false
                    binding.imageEmptyContent.isVisible = true
                    binding.labelEmptyContent.isVisible = true
                    binding.btnRefresh.isVisible = false
                    binding.progressBar.hide()
                    binding.swipeRefresh.isRefreshing = false
                    binding.swipeRefresh.isEnabled = enableSwipeRefresh
                }
                State.ERROR -> {
                    binding.list.isVisible = false
                    binding.imageEmptyContent.isVisible = false
                    binding.labelEmptyContent.isVisible = false
                    binding.btnRefresh.isVisible = true
                    binding.progressBar.hide()
                    binding.swipeRefresh.isEnabled = false
                }
            }
        }

    private val binding: ViewListExtBinding

    private val enableSwipeRefresh: Boolean

    init {
        val inflate = LayoutInflater.from(context)
        binding = ViewListExtBinding.inflate(inflate, this)

        context.theme.obtainStyledAttributes(attrs, R.styleable.ListViewEx, 0, 0).apply {
            try {
                enableSwipeRefresh = getBoolean(R.styleable.ListViewEx_enable_swipe_refresh, false)
                binding.labelEmptyContent.text = getText(R.styleable.ListViewEx_label_empty)
                var emptyImage = getDrawable(R.styleable.ListViewEx_image_empty)
                if (emptyImage == null) {
                    emptyImage = ContextCompat.getDrawable(context, R.drawable.ic_empty_content)
                }
                binding.imageEmptyContent.setImageDrawable(emptyImage)
            } finally {
                recycle()
            }
        }

        binding.swipeRefresh.setColorSchemeColors(
            ContextCompat.getColor(context, R.color.colorPrimary),
            ContextCompat.getColor(context, R.color.colorPrimaryVariant),
            ContextCompat.getColor(context, R.color.colorPrimary)
        )
        binding.swipeRefresh.isRefreshing = false
        binding.swipeRefresh.isEnabled = enableSwipeRefresh

    }

    fun setOnRefreshListener(listener: () -> Unit) {
        binding.btnRefresh.setOnClickListener {
            listener.invoke()
        }
        binding.swipeRefresh.setOnRefreshListener {
            listener.invoke()
        }
    }

    var isRefreshing: Boolean
        get() = binding.swipeRefresh.isRefreshing
        set(value) {
            binding.swipeRefresh.isRefreshing = value
        }


    val list: RecyclerView
        get() = binding.list


    enum class State {
        UNKNOWN, PROGRESS, CONTENT, EMPTY, ERROR
    }
}