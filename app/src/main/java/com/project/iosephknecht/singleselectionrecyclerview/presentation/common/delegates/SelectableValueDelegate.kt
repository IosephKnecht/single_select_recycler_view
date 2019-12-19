package com.project.iosephknecht.singleselectionrecyclerview.presentation.common.delegates

import com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.view.CustomEditTextView
import com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.viewModel.SelectableViewState

class SelectableValueDelegate(
    private val canBeModified: Boolean
) : AbstractAdapterDelegate<SelectableValueDelegate.ViewProvider, SelectableViewState> {

    interface ViewProvider : AbstractAdapterDelegate.BaseViewProvider {
        val customEditText: CustomEditTextView

        fun bindValueTextWatcher(viewState: SelectableViewState) {}
        fun unbindValueTextWatcher() {}
    }

    override fun bind(viewProvider: ViewProvider, element: SelectableViewState) {
        with(viewProvider) {
            unbindValueTextWatcher()

            val customEditTextState = when {
                !canBeModified -> CustomEditTextView.State.READABLE
                element.isLoading -> CustomEditTextView.State.LOADING
                element.isSelected -> CustomEditTextView.State.EDITABLE
                else -> CustomEditTextView.State.READABLE
            }

            customEditText.apply {
                setText(element.changedValue)
                setState(customEditTextState)
            }

            bindValueTextWatcher(element)
        }
    }
}