package com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.iosephknecht.singleselectionrecyclerview.data.SomeCategory
import com.project.iosephknecht.singleselectionrecyclerview.data.SomeModel
import com.project.iosephknecht.singleselectionrecyclerview.domain.SomeModelDataSource
import com.project.iosephknecht.singleselectionrecyclerview.domain.ValidateService
import com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.contract.FullModifiedContract
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.base_selectable.controller.SingleSelectionController
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.base_selectable.viewModel.BaseSelectableViewModel
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.viewState.SelectableViewState
import io.reactivex.Single
import java.util.*
import kotlin.collections.ArrayList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

internal class FullModifiedViewModel(
    private val someModelDataSource: SomeModelDataSource,
    private val validateService: ValidateService
) : BaseSelectableViewModel<UUID, SelectableViewState>(),
    FullModifiedContract.ViewModel,
    SingleSelectionController.ViewController<UUID, SelectableViewState> {

    override val items = MutableLiveData<Collection<SelectableViewState>>()
    override val addState = MutableLiveData(false)
    override val changedItems = MutableLiveData<Collection<UUID>>()
    override val confirmRemoveDialog = MutableLiveData<SelectableViewState>()

    override val stateController = SingleSelectionController(
        items = mapToViewStates(),
        viewController = this
    )

    private var validateDisposable: Disposable? = null

    override fun onCleared() {
        stateController.release()
    }

    override fun select(viewState: SelectableViewState) {
        validateDisposable?.dispose()

        stateController.selectItem(viewState.identifier)
    }

    override fun add() {
        items.value?.also {
            val newIndex = it.size

            stateController.addItem(
                SelectableViewState(
                    someModel = SomeModel(
                        UUID.randomUUID(),
                        label = SomeCategory.CATEGORY1,
                        value = "value_${newIndex}"
                    )
                )
            )
        }
    }

    override fun confirmAdd() {
        stateController.confirmAddItem()
    }

    override fun remove(selectableViewState: SelectableViewState) {
        stateController.removeItem(selectableViewState)
    }

    override fun confirmRemove() {
        stateController.confirmRemove()
    }

    override fun declineRemove() {
        confirmRemoveDialog.value = null
        stateController.declineRemove()
    }

    override fun applyChanges(viewState: SelectableViewState) {
        validate(viewState)
    }

    override fun onFullUpdate(list: Collection<SelectableViewState>) {
        this.items.value = ArrayList(list)
    }

    override fun onSoftUpdate(list: Collection<UUID>) {
        changedItems.value = list
    }

    override fun onRemove(viewState: SelectableViewState) {
        confirmRemoveDialog.value = viewState
    }

    override fun onAddNewElement(willBeAdded: Boolean) {
        this.addState.value = willBeAdded
    }

    override fun onReset(viewState: SelectableViewState) {
        viewState.takeIf { it.hasChanges() }?.reset(
            changedLabel = viewState.someModel.label,
            changedValue = viewState.someModel.value
        )
    }

    private fun validate(viewState: SelectableViewState) {

        if (viewState.hasChanges()) {
            val newModel = viewState.buildChangedModel()

            markAsLoading(viewState)

            validateDisposable?.dispose()

            validateDisposable = Single.just(newModel)
                .flatMap(Function<SomeModel, Single<Boolean>> { validateService.validate(it.value) })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnDispose {
                    viewState.reset(
                        changedLabel = viewState.someModel.label,
                        changedValue = viewState.someModel.value
                    )
                }
                .subscribe({ isValid ->
                    if (isValid) {
                        viewState.applyChanges(newModel)

                        if (stateController.isProcessAddState()) {
                            stateController.confirmAddItem()
                        } else {
                            stateController.resetSelected()
                        }
                    } else {
                        viewState.reset(
                            changedLabel = viewState.changedLabel,
                            changedValue = viewState.changedValue,
                            isValid = false
                        )
                        onSoftUpdate(listOf(viewState.identifier))
                    }
                }, { e ->
                    e.printStackTrace()
                })

        } else {
            if (stateController.isProcessAddState()) {
                stateController.confirmAddItem()
            } else {
                stateController.resetSelected()
            }
        }
    }

    private fun markAsLoading(viewState: SelectableViewState) {
        viewState.isLoading = true
        onSoftUpdate(listOf(viewState.identifier))
    }

    private fun SelectableViewState.buildChangedModel(): SomeModel {
        val label = changedLabel
        val value = changedValue

        return SomeModel(
            uuid = identifier,
            label = label,
            value = value.toString()
        )
    }

    override fun mapToViewStates(): Collection<SelectableViewState> {
        return someModelDataSource.generateSomeModelList(100)
            .map { list ->
                list.map { SelectableViewState(it) }
            }
            .blockingGet()
    }
}