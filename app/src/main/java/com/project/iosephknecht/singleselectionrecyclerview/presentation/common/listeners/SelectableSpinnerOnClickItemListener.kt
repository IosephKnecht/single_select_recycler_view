package com.project.iosephknecht.singleselectionrecyclerview.presentation.common.listeners

import android.view.View
import android.widget.AdapterView
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.viewState.SelectableViewState

/**
 * Implementation of [AdapterView.OnItemSelectedListener] for [SelectableViewState].
 *
 * P.S Two-way binding has already been invented
 * @see [https://developer.android.com/topic/libraries/data-binding/two-way]
 *
 * @author IosephKnecht
 */
internal class SelectableSpinnerOnClickItemListener(
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