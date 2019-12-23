package com.project.iosephknecht.singleselectionrecyclerview.presentation.common.delegates

import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.ui.CustomEditTextView
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.viewState.SelectableViewState

class SelectableValueDelegate(
    private val defaultState: CustomEditTextView.State,
    private val selectedState: CustomEditTextView.State? = null
) : AbstractAdapterDelegate<SelectableValueDelegate.ViewProvider, SelectableViewState> {

    interface ViewProvider : AbstractAdapterDelegate.BaseViewProvider {
        val customEditText: CustomEditTextView

        fun bindValueTextWatcher(viewState: SelectableViewState) {}
        fun unbindValueTextWatcher() {}
    }

    override fun bind(viewProvider: ViewProvider, element: SelectableViewState) {
        with(viewProvider) {
            unbindValueTextWatcher()

            val state = when {
                element.isLoading -> CustomEditTextView.State.LOADING
                element.isSelected -> selectedState ?: defaultState
                else -> defaultState
            }

            customEditText.apply {
                setText(element.changedValue)
                setState(state)
            }

            bindValueTextWatcher(element)
        }
    }
}