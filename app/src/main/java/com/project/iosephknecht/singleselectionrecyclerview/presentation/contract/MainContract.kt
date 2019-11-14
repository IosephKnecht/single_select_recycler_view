package com.project.iosephknecht.singleselectionrecyclerview.presentation.contract

import androidx.lifecycle.LiveData
import com.project.iosephknecht.singleselectionrecyclerview.presentation.model.ItemAction
import com.project.iosephknecht.singleselectionrecyclerview.presentation.viewModel.SelectableViewState
import java.util.*

interface MainContract {
    interface ViewModel {
        val items: LiveData<List<SelectableViewState>>
        val addState: LiveData<Boolean>
        val diff: LiveData<Array<Pair<Int, ItemAction>>>
        val confirmRemoveDialog: LiveData<SelectableViewState>

        fun select(uuid: UUID, adapterPosition: Int)
        fun add()
        fun confirmAdd()
        fun remove(selectableViewState: SelectableViewState)
        fun confirmRemove()
        fun declineRemove()
    }
}