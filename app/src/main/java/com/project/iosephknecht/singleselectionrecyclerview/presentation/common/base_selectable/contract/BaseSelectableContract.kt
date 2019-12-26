package com.project.iosephknecht.singleselectionrecyclerview.presentation.common.base_selectable.contract

import androidx.lifecycle.LiveData
import com.project.iosephknecht.single_selection.controller.SelectableItem
import java.io.Serializable

/**
 * Common contract for module with a list of [SelectableItem].
 *
 * @author IosephKnecht
 */
interface BaseSelectableContract {

    interface ViewModel<I : Serializable, T : SelectableItem<I>> {
        val items: LiveData<Collection<T>>
        val changedItems: LiveData<Collection<I>>

        fun select(viewState: T)
    }
}