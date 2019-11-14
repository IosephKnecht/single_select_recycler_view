package com.project.iosephknecht.singleselectionrecyclerview.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    override fun select(uuid: UUID) {
        stateController.selectItem(uuid)
    }

    override fun add() {
        items.value?.also {
            val newIndex = it.size

            stateController.add(
                SelectableViewState(
                    someModel = SomeModel(
                        UUID.randomUUID(),
                        label = "label_${newIndex}",
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

    override fun onUpdate(list: List<SelectableViewState>, addNewElement: Boolean) {
        this.items.value = ArrayList(list)
        this.addState.value = addNewElement
    }

    override fun onSingleSelect(uuid: UUID) {
        diff.value = arrayOf(uuid)
    }

    override fun onSwapSelect(unselectUUID: UUID, selectUUID: UUID) {
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
        val label = "label"
        val value = "value"

        for (i in 0..100) {
            list.add(
                SomeModel(
                    uuid = UUID.randomUUID(),
                    label = "${label}_$i",
                    value = "${value}_$i"
                )
            )
        }

        return list
    }
}