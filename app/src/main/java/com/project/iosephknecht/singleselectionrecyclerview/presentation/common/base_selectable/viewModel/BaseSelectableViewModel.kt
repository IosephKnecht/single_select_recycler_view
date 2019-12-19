package com.project.iosephknecht.singleselectionrecyclerview.presentation.common.base_selectable.viewModel

import androidx.lifecycle.ViewModel
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.base_selectable.controller.SelectableItem
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.base_selectable.controller.SingleSelectionController
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.base_selectable.contract.BaseSelectableContract
import java.io.Serializable

internal abstract class BaseSelectableViewModel<I : Serializable, T : SelectableItem<I>> :
    ViewModel(),
    BaseSelectableContract.ViewModel<I, T>,
    SingleSelectionController.ViewController<I, T> {

    protected abstract val stateController: SingleSelectionController<I, T>

    protected abstract fun mapToViewStates(): Collection<T>

    override fun onCleared() {
        super.onCleared()
        stateController.release()
    }

    override fun select(viewState: T) {
        stateController.selectItem(viewState.identifier)
    }
}