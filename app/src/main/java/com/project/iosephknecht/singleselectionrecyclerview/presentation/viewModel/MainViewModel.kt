package com.project.iosephknecht.singleselectionrecyclerview.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.iosephknecht.singleselectionrecyclerview.data.SomeCategory
import com.project.iosephknecht.singleselectionrecyclerview.data.SomeModel
import com.project.iosephknecht.singleselectionrecyclerview.domain.ValidateService
import com.project.iosephknecht.singleselectionrecyclerview.presentation.contract.MainContract
import com.project.iosephknecht.singleselectionrecyclerview.presentation.view.StateController
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
    StateController.ViewController<SelectableViewState> {

    private val generatedList by lazy {
        generateList().map {
            SelectableViewState(
                it
            )
        }
    }

    private val stateController = StateController(
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

        stateController.selectableItem
            ?.takeIf { it.hasChanges() }
            ?.reset()

        stateController.selectItem(viewState.uuid)
    }

    override fun add() {
        items.value?.also {
            val newIndex = it.size

            stateController.add(
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
        stateController.confirmAdd()
    }

    override fun remove(selectableViewState: SelectableViewState) {
        stateController.remove(selectableViewState)
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

    override fun onUpdate(list: List<SelectableViewState>, addNewElement: Boolean) {
        this.items.value = ArrayList(list)
        this.addState.value = addNewElement
    }

    override fun onSingleChange(uuid: UUID) {
        diff.value = arrayOf(uuid)
    }

    override fun onPairChange(unselectUUID: UUID, selectUUID: UUID) {
        diff.value = arrayOf(
            unselectUUID,
            selectUUID
        )
    }

    override fun onRemove(model: SelectableViewState) {
        confirmRemoveDialog.value = model
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

                        if (stateController.needAddConfirm()) {
                            stateController.confirmAdd()
                        } else {
                            stateController.resetSelect()
                        }
                    } else {
                        // FIXME: bottleneck
                        viewState.isValid = false
                        viewState.isLoading = false
                        onSingleChange(viewState.uuid)
                    }
                }, { e ->
                    e.printStackTrace()
                })

        } else {
            if (stateController.needAddConfirm()) {
                stateController.confirmAdd()
            } else {
                stateController.resetSelect()
            }
        }
    }

    private fun markAsLoading(viewState: SelectableViewState) {
        viewState.isLoading = true
        onSingleChange(viewState.uuid)
    }

    private fun SelectableViewState.buildChangedModel(): SomeModel {
        val label = changedLabel
        val value = changedValue

        return SomeModel(
            uuid = uuid,
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