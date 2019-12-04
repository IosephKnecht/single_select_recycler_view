package com.project.iosephknecht.singleselectionrecyclerview.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.iosephknecht.singleselectionrecyclerview.data.SomeCategory
import com.project.iosephknecht.singleselectionrecyclerview.data.SomeModel
import com.project.iosephknecht.singleselectionrecyclerview.domain.ValidateService
import com.project.iosephknecht.singleselectionrecyclerview.presentation.contract.MainContract
import com.project.iosephknecht.singleselectionrecyclerview.presentation.view.SingleSelectionController
import io.reactivex.Single
import java.util.*
import kotlin.collections.ArrayList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

class MainViewModel(
    private val validateService: ValidateService
) : ViewModel(),
    MainContract.ViewModel,
    SingleSelectionController.ViewController<UUID, SelectableViewState> {

    private val generatedList by lazy {
        generateList().map {
            SelectableViewState(
                it
            )
        }
    }

    private val stateController = SingleSelectionController(
        items = generatedList,
        viewController = this
    )

    private var validateDisposable: Disposable? = null

    override val items = MutableLiveData(generatedList)
    override val addState = MutableLiveData(false)
    override val diff = MutableLiveData<Array<UUID>>()
    override val confirmRemoveDialog = MutableLiveData<SelectableViewState>()

    override fun onCleared() {
        stateController.release()
    }

    override fun select(viewState: SelectableViewState) {
        validateDisposable?.dispose()

        stateController.currentSelectedItem
            ?.takeIf { it.hasChanges() }
            ?.reset()

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
    }

    override fun applyChanges(viewState: SelectableViewState) {
        validate(viewState)
    }

    override fun onUpdate(list: Collection<SelectableViewState>, addNewElement: Boolean) {
        this.items.value = ArrayList(list)
        this.addState.value = addNewElement
    }

    override fun onSingleChange(identifier: UUID) {
        diff.value = arrayOf(identifier)
    }

    override fun onPairChange(unselectIdentifier: UUID, selectIdentifier: UUID) {
        diff.value = arrayOf(
            unselectIdentifier,
            selectIdentifier
        )
    }

    override fun onRemove(viewState: SelectableViewState) {
        confirmRemoveDialog.value = viewState
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
                    // FIXME: bottleneck
                    viewState.reset()
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
                        // FIXME: bottleneck
                        viewState.isValid = false
                        viewState.isLoading = false
                        onSingleChange(viewState.identifier)
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
        onSingleChange(viewState.identifier)
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

    private fun generateList(): List<SomeModel> {
        val list = mutableListOf<SomeModel>()
        val value = "value"

        for (i in 0..100) {
            list.add(
                SomeModel(
                    uuid = UUID.randomUUID(),
                    label = SomeCategory.CATEGORY1,
                    value = "${value}_$i"
                )
            )
        }

        return list
    }
}