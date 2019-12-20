package com.project.iosephknecht.singleselectionrecyclerview.presentation.common.delegates

import androidx.annotation.ColorRes
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.viewState.SelectableViewState

class SelectableBackgroundDelegate(
    @ColorRes
    private val unselectableBackground: Int,
    @ColorRes
    private val selectableBackground: Int
) : AbstractAdapterDelegate<AbstractAdapterDelegate.BaseViewProvider, SelectableViewState> {

    override fun bind(
        viewProvider: AbstractAdapterDelegate.BaseViewProvider,
        element: SelectableViewState
    ) {
        val background = if (element.isSelected) selectableBackground else unselectableBackground
        viewProvider.rootView.setBackgroundResource(background)
    }
}