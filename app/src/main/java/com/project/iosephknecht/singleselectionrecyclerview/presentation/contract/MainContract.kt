package com.project.iosephknecht.singleselectionrecyclerview.presentation.contract

import androidx.lifecycle.LiveData
import com.project.iosephknecht.singleselectionrecyclerview.presentation.viewModel.SelectableViewState
import java.util.*

interface MainContract {
    interface ViewModel {
        val items: LiveData<List<SelectableViewState>>
        val addState: LiveData<Boolean>

        fun select(uuid: UUID)
        fun add()
        fun confirmAdd()
    }
}