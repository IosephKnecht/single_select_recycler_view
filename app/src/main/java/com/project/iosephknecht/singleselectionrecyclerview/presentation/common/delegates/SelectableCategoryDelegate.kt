package com.project.iosephknecht.singleselectionrecyclerview.presentation.common.delegates

import android.widget.Spinner
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.viewState.SelectableViewState

class SelectableCategoryDelegate :
    AbstractAdapterDelegate<SelectableCategoryDelegate.ViewProvider, SelectableViewState> {

    interface ViewProvider : AbstractAdapterDelegate.BaseViewProvider {
        val spinner: Spinner

        fun bindSpinnerClickListener(viewState: SelectableViewState) {}
        fun unbindSpinnerClickListener() {}
    }

    override fun bind(viewProvider: ViewProvider, element: SelectableViewState) {
        with(viewProvider) {
            unbindSpinnerClickListener()

            spinner.apply {
                setSelection(
                    element.changedLabel.ordinal
                )

                isEnabled = element.isEditableLabel && element.isSelected
            }

            bindSpinnerClickListener(element)
        }
    }
}