package com.project.iosephknecht.singleselectionrecyclerview.presentation.only_selection.viewModel

import androidx.lifecycle.MutableLiveData
import com.project.iosephknecht.singleselectionrecyclerview.domain.SomeModelDataSource
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.base_selectable.viewModel.BaseSelectableViewModel
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.base_selectable.controller.SingleSelectionController
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.helpers.SingleLiveEvent
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.viewState.SelectableViewState
import com.project.iosephknecht.singleselectionrecyclerview.presentation.only_selection.contract.OnlySelectionContract
import java.util.*
import kotlin.collections.ArrayList

internal class OnlySelectionViewModel(
    private val someModelDataSource: SomeModelDataSource
) : BaseSelectableViewModel<UUID, SelectableViewState>(),
    OnlySelectionContract.ViewModel {

    override val items = MutableLiveData<Collection<SelectableViewState>>()
    override val changedItems = SingleLiveEvent<Collection<UUID>>()

    override val stateController = SingleSelectionController(
        items = mapToViewStates(),
        viewController = this
    )

    override fun onSoftUpdate(list: Collection<UUID>) {
        changedItems.value = list
    }

    override fun onFullUpdate(list: Collection<SelectableViewState>) {
        items.value = ArrayList(list)
    }

    override fun mapToViewStates(): Collection<SelectableViewState> {
        return someModelDataSource.generateSomeModelList(100)
            .map { models -> models.map {
                SelectableViewState(
                    it
                )
            } }
            .blockingGet()
    }
}