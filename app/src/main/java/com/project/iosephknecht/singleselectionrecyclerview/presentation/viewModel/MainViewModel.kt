package com.project.iosephknecht.singleselectionrecyclerview.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.iosephknecht.singleselectionrecyclerview.data.SomeCategory
import com.project.iosephknecht.singleselectionrecyclerview.data.SomeModel
import com.project.iosephknecht.singleselectionrecyclerview.presentation.contract.MainContract
import com.project.iosephknecht.singleselectionrecyclerview.presentation.view.StateController
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel : ViewModel(),
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


    override val items = MutableLiveData(generatedList)
    override val addState = MutableLiveData(false)
    override val diff = MutableLiveData<Array<UUID>>()
    override val confirmRemoveDialog = MutableLiveData<SelectableViewState>()

    override fun onCleared() {
        stateController.release()
    }

    override fun select(viewState: SelectableViewState) {
        stateController.selectableItem
            ?.takeIf { it.hasChanges() }
            ?.also {
                it.changedValue = null
            }

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
        viewState.takeIf { it.hasChanges() }
            ?.apply {
                someModel = SomeModel(
                    uuid = uuid,
                    label = originalLabel,
                    value = changedValue!!.toString()
                )

                changedValue = null
            }

        if (stateController.needAddConfirm()) {
            stateController.confirmAdd()
        } else {
            stateController.resetSelect()
        }
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