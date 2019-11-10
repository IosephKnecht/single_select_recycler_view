package com.project.iosephknecht.singleselectionrecyclerview.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.iosephknecht.singleselectionrecyclerview.data.SelectableModel
import com.project.iosephknecht.singleselectionrecyclerview.view.StateController
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel : ViewModel(),
    StateController.ViewController {

    private val generatedList by lazy {
        generateList().map {
            SelectableViewState(
                it
            )
        }
    }

    private val stateController =
        StateController(
            items = generatedList,
            viewController = this
        )


    val items = MutableLiveData<List<SelectableViewState>>().apply {
        value = generatedList
    }

    fun select(uuid: UUID) {
        stateController.selectItem(uuid)
    }

    override fun onCleared() {
        stateController.release()
    }

    override fun onUpdate(list: List<SelectableViewState>) {
        items.value = ArrayList(list)
    }


    private fun generateList(): List<SelectableModel> {
        val list = mutableListOf<SelectableModel>()
        val label = "label"
        val value = "value"

        for (i in 0..100) {
            list.add(
                SelectableModel(
                    uuid = UUID.randomUUID(),
                    label = "${label}_$i",
                    value = "${value}_$i"
                )
            )
        }

        return list
    }
}