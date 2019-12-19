package com.project.iosephknecht.singleselectionrecyclerview.presentation.common.delegates

import android.view.View

interface AbstractAdapterDelegate<ViewProvider : AbstractAdapterDelegate.BaseViewProvider, Element> {
    interface BaseViewProvider {
        val rootView: View
    }

    fun bind(viewProvider: ViewProvider, element: Element)
}