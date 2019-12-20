package com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.view.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import com.project.iosephknecht.singleselectionrecyclerview.R
import com.project.iosephknecht.singleselectionrecyclerview.data.SomeCategory
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.delegates.*
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.listeners.SelectableSpinnerOnClickItemListener
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.listeners.SelectableViewStateTextWatcher
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.ui.CustomEditTextView
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.viewState.SelectableViewState
import com.project.iosephknecht.singleselectionrecyclerview.presentation.inflate
import java.util.*

class FullModifiedAdapter(
    private val selectableBackgroundDelegate: SelectableBackgroundDelegate,
    private val selectableClickManagerDelegate: SelectableClickManagerDelegate,
    private val selectableValueDelegate: SelectableValueDelegate,
    private val selectableCategoryDelegate: SelectableCategoryDelegate,
    private val selectableErrorDelegate: SelectableErrorDelegate
) : RecyclerView.Adapter<FullModifiedAdapter.ViewHolder>() {

    private var items = listOf<SelectableViewState>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            itemView = inflate(parent, R.layout.item_select),
            spinnerClickListener = SelectableSpinnerOnClickItemListener { ordinal ->
                if (ordinal != changedLabel.ordinal) {
                    changedLabel = SomeCategory.values()[ordinal]
                }
            },
            valueTextWatcher = SelectableViewStateTextWatcher { value ->
                value?.takeIf { it != changedValue }?.also {
                    changedValue = it
                }
            }
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val element = items[position]

        selectableBackgroundDelegate.bind(holder, element)
        selectableClickManagerDelegate.bind(holder, element)
        selectableValueDelegate.bind(holder, element)
        selectableCategoryDelegate.bind(holder, element)
        selectableErrorDelegate.bind(holder, element)
    }

    override fun getItemCount() = items.size

    fun reload(items: Collection<SelectableViewState>) {
        this.items = items.toList()
        notifyDataSetChanged()
    }

    fun applyChanges(changes: Collection<UUID>) {
        val positions = mutableListOf<Int>()

        items.forEachIndexed { index, viewState ->
            if (changes.contains(viewState.identifier)) {
                positions.add(index)
            }

            if (changes.size == positions.size) return@forEachIndexed
        }

        positions.forEach {
            notifyItemChanged(it)
        }
    }

    class ViewHolder(
        itemView: View,
        private val spinnerClickListener: SelectableSpinnerOnClickItemListener,
        private val valueTextWatcher: SelectableViewStateTextWatcher
    ) : RecyclerView.ViewHolder(itemView),
        SelectableClickManagerDelegate.ViewProvider,
        SelectableValueDelegate.ViewProvider,
        SelectableCategoryDelegate.ViewProvider,
        SelectableErrorDelegate.ViewProvider {

        override val rootView: View = itemView

        override val customEditText: CustomEditTextView =
            itemView.findViewById(R.id.custom_edit_text_view)

        override val spinner: Spinner = itemView.findViewById(R.id.label)

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

            spinner.adapter = arrayAdapter
        }

        override fun bindValueTextWatcher(viewState: SelectableViewState) {
            customEditText.apply {
                valueTextWatcher.viewState = viewState
                addValueTextWatcher(valueTextWatcher)
            }
        }

        override fun unbindValueTextWatcher() {
            customEditText.apply {
                valueTextWatcher.viewState = null
                removeValueTextWatcher(valueTextWatcher)
            }
        }

        override fun bindSpinnerClickListener(viewState: SelectableViewState) {
            spinner.apply {
                spinnerClickListener.viewState = viewState
                onItemSelectedListener = spinnerClickListener
            }
        }

        override fun unbindSpinnerClickListener() {
            spinner.apply {
                spinnerClickListener.viewState = null
                onItemSelectedListener = null
            }
        }
    }
}