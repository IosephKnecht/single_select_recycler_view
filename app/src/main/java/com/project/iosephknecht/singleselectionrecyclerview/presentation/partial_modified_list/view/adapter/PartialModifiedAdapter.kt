package com.project.iosephknecht.singleselectionrecyclerview.presentation.partial_modified_list.view.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import com.project.iosephknecht.singleselectionrecyclerview.R
import com.project.iosephknecht.singleselectionrecyclerview.data.SomeCategory
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.delegates.*
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.ui.CustomEditTextView
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.viewState.SelectableViewState
import com.project.iosephknecht.singleselectionrecyclerview.presentation.inflate
import java.util.*

/**
 * Adapter to view list with partial modified items.
 *
 * @author IosephKnecht
 */
internal class PartialModifiedAdapter(
    private val selectableBackgroundDelegate: SelectableBackgroundDelegate,
    private val selectableTranslationDelegate: SelectableTranslationDelegate,
    private val selectableClickManagerDelegate: SelectableClickManagerDelegate,
    private val selectableValueDelegate: SelectableValueDelegate,
    private val selectableCategoryDelegate: SelectableCategoryDelegate
) : RecyclerView.Adapter<PartialModifiedAdapter.ViewHolder>() {

    private var items: List<SelectableViewState> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflate(parent, R.layout.item_select))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val element = items[position]

        selectableBackgroundDelegate.bind(holder, element)
        selectableTranslationDelegate.bind(holder, element)
        selectableClickManagerDelegate.bind(holder, element)
        selectableValueDelegate.bind(holder, element)
        selectableCategoryDelegate.bind(holder, element)
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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        SelectableClickManagerDelegate.ViewProvider,
        SelectableValueDelegate.ViewProvider,
        SelectableCategoryDelegate.ViewProvider {

        override val rootView: View = itemView

        override val customEditText: CustomEditTextView =
            itemView.findViewById(R.id.custom_edit_text_view)

        override val spinner: Spinner = itemView.findViewById(R.id.label)

        init {
            val context = rootView.context

            val arrayAdapter = ArrayAdapter(
                context,
                R.layout.item_spinner_label,
                SomeCategory.values().map {
                    it.getTitle(context.resources)!!
                }
            )

            arrayAdapter.setDropDownViewResource(R.layout.item_spinner_label_dropdown)

            spinner.adapter = arrayAdapter
        }
    }
}