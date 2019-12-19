package com.project.iosephknecht.singleselectionrecyclerview.presentation.common.base_selectable.contract

import androidx.lifecycle.LiveData
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.base_selectable.controller.SelectableItem
import java.io.Serializable

interface BaseSelectableContract {

    interface ViewModel<I : Serializable, T : SelectableItem<I>> {
        val items: LiveData<Collection<T>>
        val changedItems: LiveData<Collection<I>>

        fun select(viewState: T)
    }
}