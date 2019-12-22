package com.project.iosephknecht.singleselectionrecyclerview.presentation.partial_modified_list.contract

import androidx.lifecycle.LiveData
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.base_selectable.contract.BaseSelectableContract
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.viewState.SelectableViewState
import java.util.*

interface PartialModifiedContract {

    interface ViewModel : BaseSelectableContract.ViewModel<UUID, SelectableViewState> {
        val confirmRemoveDialog: LiveData<SelectableViewState>

        fun add()
        fun remove(viewState: SelectableViewState)
        fun confirmRemove()
        fun declineRemove()
    }
}