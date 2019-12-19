package com.project.iosephknecht.singleselectionrecyclerview.presentation.common.delegates

import android.widget.Spinner
import com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.viewModel.SelectableViewState

class SelectableCategoryDelegate(
    private val canBeModified: Boolean
) :
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

                isEnabled = canBeModified
            }

            bindSpinnerClickListener(element)
        }
    }
}