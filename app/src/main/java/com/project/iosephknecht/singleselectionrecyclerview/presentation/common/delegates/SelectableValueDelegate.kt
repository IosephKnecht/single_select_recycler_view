package com.project.iosephknecht.singleselectionrecyclerview.presentation.common.delegates

import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.ui.CustomEditTextView
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.viewState.SelectableViewState

class SelectableValueDelegate :
    AbstractAdapterDelegate<SelectableValueDelegate.ViewProvider, SelectableViewState> {

    interface ViewProvider : AbstractAdapterDelegate.BaseViewProvider {
        val customEditText: CustomEditTextView

        fun bindValueTextWatcher(viewState: SelectableViewState) {}
        fun unbindValueTextWatcher() {}
    }

    override fun bind(viewProvider: ViewProvider, element: SelectableViewState) {
        with(viewProvider) {
            unbindValueTextWatcher()

            // FIXME: too many flags
            val state = when {
                element.isLoading -> CustomEditTextView.State.LOADING
                !element.isEditableValue && element.isCouldRemoved -> CustomEditTextView.State.READABLE_WITH_REMOVE
                element.isEditableValue && element.isSelected -> CustomEditTextView.State.EDITABLE
                element.isEditableValue && !element.isSelected -> CustomEditTextView.State.READABLE_WITH_REMOVE
                else -> CustomEditTextView.State.ONLY_READABLE
            }

            customEditText.apply {
                setText(element.changedValue)
                setState(state)
            }

            bindValueTextWatcher(element)
        }
    }
}