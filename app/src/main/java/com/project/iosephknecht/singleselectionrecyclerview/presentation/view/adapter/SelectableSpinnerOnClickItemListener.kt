package com.project.iosephknecht.singleselectionrecyclerview.presentation.view.adapter

import android.view.View
import android.widget.AdapterView
import com.project.iosephknecht.singleselectionrecyclerview.presentation.viewModel.SelectableViewState

class SelectableSpinnerOnClickItemListener(
    private val block: SelectableViewState.(ordinal: Int) -> Unit
) : AdapterView.OnItemSelectedListener {

    var viewState: SelectableViewState? = null

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // ignore
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        viewState?.also { block.invoke(it, position) }
    }
}