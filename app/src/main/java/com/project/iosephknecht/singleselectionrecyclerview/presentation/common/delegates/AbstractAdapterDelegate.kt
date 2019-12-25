package com.project.iosephknecht.singleselectionrecyclerview.presentation.common.delegates

import android.view.View

/**
 * Common contract for adapter's delegates.
 *
 * P.S Delegates for adapters are already invented
 * @see [https://github.com/sockeqwe/AdapterDelegates]
 *
 * @author IosephKnecht
 */
interface AbstractAdapterDelegate<ViewProvider : AbstractAdapterDelegate.BaseViewProvider, Element> {

    /**
     * Common contract for view providers.
     */
    interface BaseViewProvider {
        val rootView: View
    }

    /**
     * Bind views to model.
     *
     * @param viewProvider
     * @param element
     */
    fun bind(viewProvider: ViewProvider, element: Element)
}