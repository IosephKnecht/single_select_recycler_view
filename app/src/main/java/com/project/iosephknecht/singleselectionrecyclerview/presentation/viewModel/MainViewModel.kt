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
        creator = {
            SelectableViewState(
                someModel = SomeModel(
                    UUID.randomUUID(),
                    label = "",
                    value = ""
                )
            )
        },
        viewController = this
    )


    override val items = MutableLiveData(generatedList)
    override val addState = MutableLiveData(false)

    override fun select(uuid: UUID) {
        stateController.selectItem(uuid)
    }

    override fun add() {
        stateController.add()
    }

    override fun confirmAdd() {
        stateController.confirmSave()
    }

    override fun onCleared() {
        stateController.release()
    }

    override fun onUpdate(list: List<SelectableViewState>, addNewElement: Boolean) {
        this.items.value = ArrayList(list)
        this.addState.value = addNewElement
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