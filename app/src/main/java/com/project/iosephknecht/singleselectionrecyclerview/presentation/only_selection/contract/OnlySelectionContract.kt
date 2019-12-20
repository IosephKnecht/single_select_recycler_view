package com.project.iosephknecht.singleselectionrecyclerview.presentation.only_selection.contract

import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.base_selectable.contract.BaseSelectableContract
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.viewState.SelectableViewState
import java.util.*

interface OnlySelectionContract {
    interface ViewModel : BaseSelectableContract.ViewModel<UUID, SelectableViewState>
}