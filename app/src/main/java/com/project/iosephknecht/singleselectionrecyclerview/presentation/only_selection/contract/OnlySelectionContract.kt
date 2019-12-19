package com.project.iosephknecht.singleselectionrecyclerview.presentation.only_selection.contract

import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.base_selectable.contract.BaseSelectableContract
import com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.viewModel.SelectableViewState
import java.util.*

interface OnlySelectionContract {
    interface ViewModel : BaseSelectableContract.ViewModel<UUID, SelectableViewState>
}