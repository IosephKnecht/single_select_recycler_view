package com.project.iosephknecht.singleselectionrecyclerview.presentation.cases.view.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.iosephknecht.singleselectionrecyclerview.R
import com.project.iosephknecht.singleselectionrecyclerview.presentation.inflate
import com.project.iosephknecht.singleselectionrecyclerview.presentation.main.model.SingleSelectionCase

/**
 * An adapter for presenting a cases list.
 *
 * @author IosephKnecht
 */
class CasesAdapter(
    private val itemClick: (SingleSelectionCase) -> Unit
) :
    RecyclerView.Adapter<CasesAdapter.ViewHolder>() {

    private var items = listOf<SingleSelectionCase>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflate(parent, R.layout.item_case_layout))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val case = items[position]

        with(holder) {
            val context = itemView.context

            title.text = context.resources.getString(case.title)
            description.text = context.resources.getString(case.description)

            itemView.setOnClickListener { itemClick.invoke(case) }
        }
    }

    override fun getItemCount() = items.size

    fun reload(items: List<SingleSelectionCase>) {
        this.items = items
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val description: TextView = itemView.findViewById(R.id.description)
    }
}