package com.project.iosephknecht.singleselectionrecyclerview.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.project.iosephknecht.singleselectionrecyclerview.R
import com.project.iosephknecht.singleselectionrecyclerview.data.SomeCategory
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
            SelectableSpinnerOnClickItemListener { ordinal ->
                changedLabel = SomeCategory.values()[ordinal]
            },
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
                labelSpinner = labelSpinner,
                valueTextView = valueTextView,
                saveButton = saveButton,
                removeButton = removeButton
            )


            labelSpinner?.setSelection(
                viewState.changedLabel?.ordinal ?: viewState.originalLabel.ordinal
            )

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
        private val spinnerClickListener: SelectableSpinnerOnClickItemListener,
        private val valueTextWatcher: SelectableViewStateTextWatcher
    ) : RecyclerView.ViewHolder(itemView) {
        val labelSpinner: Spinner? = itemView.findViewById(R.id.label)
        val valueTextView: EditText? = itemView.findViewById(R.id.value)
        val removeButton: ImageView? = itemView.findViewById(R.id.delete_button)
        val saveButton: ImageView? = itemView.findViewById(R.id.save_button)

        private val arrayAdapter: ArrayAdapter<String>

        init {
            val context = itemView.context

            arrayAdapter = ArrayAdapter(
                context,
                R.layout.item_spinner_label,
                SomeCategory.values().map {
                    it.getTitle(context.resources)!!
                }
            )

            arrayAdapter.setDropDownViewResource(R.layout.item_spinner_label_dropdown)

            labelSpinner?.adapter = arrayAdapter

            labelSpinner?.setOnItemSelectedListener(spinnerClickListener)
            valueTextView?.addTextChangedListener(valueTextWatcher)
        }

        fun bind(viewState: SelectableViewState) {
            spinnerClickListener.viewState = viewState
            valueTextWatcher.viewState = viewState
        }

        fun unbind() {
            spinnerClickListener.viewState = null
            valueTextWatcher.viewState = null
        }
    }
}