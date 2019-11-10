package com.project.iosephknecht.singleselectionrecyclerview.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.iosephknecht.singleselectionrecyclerview.R
import com.project.iosephknecht.singleselectionrecyclerview.viewModel.SelectableViewState

class SelectableAdapter(
    private val selectableBinder: SelectableBinder
) : RecyclerView.Adapter<SelectableAdapter.SelectableViewHolder>() {

    private var items: List<SelectableViewState> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectableViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_select, parent, false)

        return SelectableViewHolder(
            itemView
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: SelectableViewHolder, position: Int) {
        val viewState = items[position]

        selectableBinder.bind(
            viewState,
            holder.itemView,
            holder.labelTextView,
            holder.valueTextView
        )

        with(holder) {
            labelTextView?.setText(viewState.label)
            valueTextView?.setText(viewState.value)
        }
    }

    fun reload(items: List<SelectableViewState>) {
        this.items = items
        notifyDataSetChanged()
    }


    class SelectableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val labelTextView: TextView? = itemView.findViewById<TextView>(R.id.label)
        val valueTextView: TextView? = itemView.findViewById<TextView>(R.id.value)
    }
}