package com.project.iosephknecht.singleselectionrecyclerview.presentation.common.delegates

import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.ui.CustomEditTextView
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.viewState.SelectableViewState

class SelectableErrorDelegate(
    @DrawableRes
    private val defaultBackground: Int,
    @DrawableRes
    private val errorBackground: Int
) : AbstractAdapterDelegate<SelectableErrorDelegate.ViewProvider, SelectableViewState> {

    interface ViewProvider : AbstractAdapterDelegate.BaseViewProvider {
        val customEditText: CustomEditTextView
    }

    override fun bind(viewProvider: ViewProvider, element: SelectableViewState) {
        with(viewProvider) {
            val backgroundRes = if (element.isValid) defaultBackground else errorBackground

            customEditText.setValueBackground(
                ContextCompat.getDrawable(
                    viewProvider.rootView.context,
                    backgroundRes
                )!!
            )
        }
    }
}