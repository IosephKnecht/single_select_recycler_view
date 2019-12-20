package com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.contract

import androidx.lifecycle.LiveData
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.base_selectable.contract.BaseSelectableContract
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.viewState.SelectableViewState
import java.util.*

interface FullModifiedContract {
    interface ViewModel : BaseSelectableContract.ViewModel<UUID, SelectableViewState> {
        val addState: LiveData<Boolean>
        val confirmRemoveDialog: LiveData<SelectableViewState>

        fun add()
        fun confirmAdd()
        fun remove(selectableViewState: SelectableViewState)
        fun confirmRemove()
        fun declineRemove()

        fun applyChanges(viewState: SelectableViewState)
    }
}