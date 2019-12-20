package com.project.iosephknecht.singleselectionrecyclerview.presentation.cases.view.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.iosephknecht.singleselectionrecyclerview.R
import com.project.iosephknecht.singleselectionrecyclerview.presentation.inflate
import com.project.iosephknecht.singleselectionrecyclerview.presentation.main.model.SingleSelectionCase

class CasesAdapter(
    private val clickCase1: () -> Unit,
    private val clickCase2: () -> Unit,
    private val clickCase3: () -> Unit
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

            val clickListener = when (case) {
                SingleSelectionCase.CASE1 -> View.OnClickListener { clickCase1.invoke() }
                SingleSelectionCase.CASE2 -> View.OnClickListener { clickCase2.invoke() }
                SingleSelectionCase.CASE3 -> View.OnClickListener { clickCase3.invoke() }
            }

            itemView.setOnClickListener(clickListener)
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