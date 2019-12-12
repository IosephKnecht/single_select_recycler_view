package com.project.iosephknecht.singleselectionrecyclerview.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.project.iosephknecht.singleselectionrecyclerview.R
import com.project.iosephknecht.singleselectionrecyclerview.data.SomeCategory
import com.project.iosephknecht.singleselectionrecyclerview.presentation.view.CustomEditTextView
import com.project.iosephknecht.singleselectionrecyclerview.presentation.viewModel.SelectableViewState
import java.util.*

class SelectableAdapter(
    private val selectableBinder: SelectableBinder,
    private val validateBinder: ValidateBinder
) : RecyclerView.Adapter<SelectableAdapter.SelectableViewHolder>() {

    private var items: List<SelectableViewState> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectableViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_select, parent, false)

        return SelectableViewHolder(
            itemView,
            SelectableSpinnerOnClickItemListener { ordinal ->
                if (ordinal != changedLabel?.ordinal) {
                    changedLabel = SomeCategory.values()[ordinal]
                }
            },
            SelectableViewStateTextWatcher { value ->
                value?.takeIf { it != changedValue }?.also {
                    changedValue = it
                }
            }
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: SelectableViewHolder, position: Int) {
        val viewState = items[position]

        holder.unbindValueTextWatcher()
        holder.unbindSpinnerClickListener()

        with(holder) {
            selectableBinder.bind(
                viewState = viewState,
                holder = holder
            )

            validateBinder.bind(
                holder = holder,
                isValid = viewState.isValid
            )


            labelSpinner?.setSelection(
                viewState.changedLabel.ordinal
            )

            val customEditTextState = when {
                viewState.isLoading -> CustomEditTextView.State.LOADING
                viewState.isSelected -> CustomEditTextView.State.EDITABLE
                else -> CustomEditTextView.State.READABLE
            }

            customEditText?.apply {
                setText(viewState.changedValue)
                setState(customEditTextState)
            }

            holder.bindValueTextWatcher(viewState)
            holder.bindSpinnerClickListener(viewState)
        }
    }

    override fun onViewRecycled(holder: SelectableViewHolder) {
        super.onViewRecycled(holder)

        holder.unbindValueTextWatcher()
        holder.unbindSpinnerClickListener()
    }

    fun reload(items: List<SelectableViewState>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun applyDiff(diff: Collection<UUID>) {
        val positions = mutableListOf<Int>()

        items.forEachIndexed { index, viewState ->
            if (diff.contains(viewState.identifier)) {
                positions.add(index)
            }

            if (diff.size == positions.size) return@forEachIndexed
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
        val customEditText: CustomEditTextView? = itemView.findViewById(R.id.custom_edit_text_view)

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
        }

        fun bindValueTextWatcher(viewState: SelectableViewState) {
            customEditText?.apply {
                valueTextWatcher.viewState = viewState
                addValueTextWatcher(valueTextWatcher)
            }
        }

        fun unbindValueTextWatcher() {
            customEditText?.apply {
                valueTextWatcher.viewState = null
                removeValueTextWatcher(valueTextWatcher)
            }
        }

        fun bindSpinnerClickListener(viewState: SelectableViewState) {
            labelSpinner?.apply {
                spinnerClickListener.viewState = viewState
                onItemSelectedListener = spinnerClickListener
            }
        }

        fun unbindSpinnerClickListener() {
            labelSpinner?.apply {
                spinnerClickListener.viewState = null
                onItemSelectedListener = null
            }
        }
    }
}