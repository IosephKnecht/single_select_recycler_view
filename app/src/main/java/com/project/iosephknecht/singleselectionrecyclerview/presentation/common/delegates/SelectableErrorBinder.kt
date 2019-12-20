package com.project.iosephknecht.singleselectionrecyclerview.presentation.common.delegates

import androidx.annotation.DrawableRes
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.ui.CustomEditTextView
import com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.viewModel.SelectableViewState

class SelectableErrorBinder(
    @DrawableRes
    private val defaultBackground: Int,
    @DrawableRes
    private val errorBackground: Int
) : AbstractAdapterDelegate<SelectableErrorBinder.ViewProvider, SelectableViewState> {

    interface ViewProvider : AbstractAdapterDelegate.BaseViewProvider {
        val customEditText: CustomEditTextView
    }

    override fun bind(viewProvider: ViewProvider, element: SelectableViewState) {
        with(viewProvider) {
            val backgroundRes = if (element.isValid) defaultBackground else errorBackground

            customEditText.setBackgroundResource(backgroundRes)
        }
    }
}