package com.project.iosephknecht.singleselectionrecyclerview.presentation.view.adapter

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.project.iosephknecht.singleselectionrecyclerview.R
import com.project.iosephknecht.singleselectionrecyclerview.presentation.viewModel.SelectableViewState
import java.util.*

class SelectableBinder(
    private val selectableAction: (uuid: UUID, adapterPosition: Int) -> Unit
) {
    fun bind(
        position: Int,
        viewState: SelectableViewState,
        itemView: View,
        labelTextView: TextView?,
        valueTextView: TextView?
    ) {
        val context = itemView.context

        val clickListener = View.OnClickListener {
            selectableAction.invoke(viewState.uuid, position)
        }.takeIf { !viewState.isSelected }

        itemView.setOnClickListener(clickListener)
        itemView.translationZ = if (viewState.isSelected) 10f else 0f

        itemView.setBackgroundColor(
            ContextCompat.getColor(
                context,
                if (viewState.isSelected) R.color.accent else android.R.color.white
            )
        )
    }
}