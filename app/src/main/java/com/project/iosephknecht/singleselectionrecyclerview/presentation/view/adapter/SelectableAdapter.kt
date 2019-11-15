package com.project.iosephknecht.singleselectionrecyclerview.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.iosephknecht.singleselectionrecyclerview.R
import com.project.iosephknecht.singleselectionrecyclerview.presentation.viewModel.SelectableViewState
import java.util.*

class SelectableAdapter(
    private val selectableBinder: SelectableBinder
) : RecyclerView.Adapter<SelectableAdapter.SelectableViewHolder>() {

    private var items: List<SelectableViewState> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectableViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_select, parent, false)

        return SelectableViewHolder(
            itemView,
            SelectableViewStateTextWatcher { value ->
                changedValue = value
            }
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: SelectableViewHolder, position: Int) {
        val viewState = items[position]

        with(holder) {
            selectableBinder.bind(
                viewState = viewState,
                itemView = itemView,
                labelTextView = labelTextView,
                valueTextView = valueTextView,
                saveButton = saveButton,
                removeButton = removeButton
            )


            labelTextView?.setText(viewState.label)

            valueTextView?.apply {
                holder.bind(viewState)
                setText(viewState.changedValue ?: viewState.originalValue)
            }

            saveButton?.visibility = if (viewState.isSelected) View.VISIBLE else View.GONE
        }
    }

    override fun onViewRecycled(holder: SelectableViewHolder) {
        super.onViewRecycled(holder)

        holder.unbind()
    }

    fun reload(items: List<SelectableViewState>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun applyDiff(diff: Array<UUID>) {
        val positions = mutableListOf<Int>()

        items.forEachIndexed { index, viewState ->
            if (diff.contains(viewState.uuid)) {
                positions.add(index)
            }
        }

        positions.forEach {
            notifyItemChanged(it)
        }
    }


    class SelectableViewHolder(
        itemView: View,
        private val valueTextWatcher: SelectableViewStateTextWatcher
    ) : RecyclerView.ViewHolder(itemView) {

        val labelTextView: TextView? = itemView.findViewById(R.id.label)
        val valueTextView: EditText? = itemView.findViewById(R.id.value)
        val removeButton: ImageView? = itemView.findViewById(R.id.delete_button)
        val saveButton: ImageView? = itemView.findViewById(R.id.save_button)

        init {
            valueTextView?.addTextChangedListener(valueTextWatcher)
        }

        fun bind(viewState: SelectableViewState) {
            valueTextWatcher.viewState = viewState
        }

        fun unbind() {
            valueTextWatcher.viewState = null
        }
    }
}