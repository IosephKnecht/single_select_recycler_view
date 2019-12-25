package com.project.iosephknecht.singleselectionrecyclerview.presentation.common.delegates

import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.viewState.SelectableViewState

/**
 * Implementation of [AbstractAdapterDelegate] for changing translationZ param.
 *
 * @author IosephKnecht
 */
internal class SelectableTranslationDelegate(
    private val unselectedTranslationZ: Float,
    private val selectedTranslationZ: Float
) : AbstractAdapterDelegate<AbstractAdapterDelegate.BaseViewProvider, SelectableViewState> {

    override fun bind(
        viewProvider: AbstractAdapterDelegate.BaseViewProvider,
        element: SelectableViewState
    ) {
        val translationZ = if (element.isSelected) selectedTranslationZ else unselectedTranslationZ

        viewProvider.rootView.translationZ = translationZ
    }
}