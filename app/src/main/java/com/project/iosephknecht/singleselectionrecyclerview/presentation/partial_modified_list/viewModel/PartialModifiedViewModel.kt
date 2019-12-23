package com.project.iosephknecht.singleselectionrecyclerview.presentation.partial_modified_list.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.project.iosephknecht.singleselectionrecyclerview.domain.SomeModelDataSource
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.base_selectable.controller.SingleSelectionController
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.base_selectable.viewModel.BaseSelectableViewModel
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.helpers.SingleLiveEvent
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.viewState.SelectableViewState
import com.project.iosephknecht.singleselectionrecyclerview.presentation.partial_modified_list.contract.PartialModifiedContract
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

internal class PartialModifiedViewModel(
    private val someModelDataSource: SomeModelDataSource
) : BaseSelectableViewModel<UUID, SelectableViewState>(),
    PartialModifiedContract.ViewModel {

    override val items = MutableLiveData<Collection<SelectableViewState>>()
    override val changedItems = MutableLiveData<Collection<UUID>>()
    override val confirmRemoveDialog = SingleLiveEvent<SelectableViewState>()

    override val stateController = SingleSelectionController(
        items = mapToViewStates(),
        viewController = this
    )

    private var generateDisposable: Disposable? = null

    override fun add() {
        val index = items.value?.size ?: 0

        generateDisposable?.dispose()
        generateDisposable = generateViewState(index)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { viewState ->
                    stateController.addItem(viewState)
                    stateController.confirmAddItem()
                },
                {
                    Log.e(this::class.qualifiedName, it.localizedMessage, it)
                }
            )
    }

    override fun remove(viewState: SelectableViewState) {
        stateController.removeItem(viewState)
    }

    override fun confirmRemove() {
        stateController.confirmRemove()
    }

    override fun declineRemove() {
        confirmRemoveDialog.value = null
        stateController.declineRemove()
    }

    override fun onSoftUpdate(list: Collection<UUID>) {
        changedItems.value = list
    }

    override fun onFullUpdate(list: Collection<SelectableViewState>) {
        this.items.value = ArrayList(list)
    }

    override fun onRemove(viewState: SelectableViewState) {
        confirmRemoveDialog.value = viewState
    }

    override fun mapToViewStates(): Collection<SelectableViewState> {
        return someModelDataSource.generateSomeModelList(100)
            .map { list ->
                list.map {
                    SelectableViewState(
                        someModel = it,
                        isEditableValue = false,
                        isEditableLabel = false
                    )
                }
            }
            .blockingGet()
    }

    private fun generateViewState(index: Int): Single<SelectableViewState> {
        return someModelDataSource.generateSomeModel(index)
            .map {
                SelectableViewState(
                    someModel = it,
                    isEditableValue = false,
                    isEditableLabel = false
                )
            }
    }
}