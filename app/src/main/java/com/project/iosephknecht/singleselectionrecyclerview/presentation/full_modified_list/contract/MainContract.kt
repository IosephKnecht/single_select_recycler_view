package com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.contract

import androidx.lifecycle.LiveData
import com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.viewModel.SelectableViewState
import java.util.*

interface MainContract {
    interface ViewModel {
        val items: LiveData<List<SelectableViewState>>
        val addState: LiveData<Boolean>
        val diff: LiveData<Collection<UUID>>
        val confirmRemoveDialog: LiveData<SelectableViewState>

        fun select(viewState: SelectableViewState)
        fun add()
        fun confirmAdd()
        fun remove(selectableViewState: SelectableViewState)
        fun confirmRemove()
        fun declineRemove()

        fun applyChanges(viewState: SelectableViewState)
    }
}