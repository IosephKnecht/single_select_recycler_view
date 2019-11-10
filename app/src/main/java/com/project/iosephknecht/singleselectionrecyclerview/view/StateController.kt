package com.project.iosephknecht.singleselectionrecyclerview.view

import com.project.iosephknecht.singleselectionrecyclerview.viewModel.SelectableViewState
import java.util.*
import kotlin.collections.ArrayList

class StateController(
    items: List<SelectableViewState>,
    private val viewController: ViewController
) {

    private var currentState: State = Unselected()
    private var selectedItem: SelectableViewState? = null

    private val mutableItems: MutableList<SelectableViewState> = ArrayList(items)

    fun selectItem(uuid: UUID) {
        currentState.select(uuid)
    }

    fun release() {
        currentState.release()
    }

    interface ViewController {
        fun onUpdate(list: List<SelectableViewState>)
    }

    private interface State {
        fun select(uuid: UUID) {}
        fun release() {}
    }

    private inner class Unselected :
        State {
        override fun select(uuid: UUID) {
            mutableItems.find { it.uuid == uuid }?.also { viewState ->
                selectedItem = viewState.apply {
                    isSelected = true
                }

                viewController.onUpdate(mutableItems)

                currentState = Selected()
            }
        }

        override fun release() {
            selectedItem = null
            currentState = Release()
        }
    }

    private inner class Selected :
        State {
        override fun select(uuid: UUID) {
            if (selectedItem!!.uuid == uuid) return

            mutableItems.find { it.uuid == uuid }?.also { viewState ->
                selectedItem!!.isSelected = false
                selectedItem = viewState.apply {
                    isSelected = true
                }

                viewController.onUpdate(mutableItems)
            }
        }

        override fun release() {
            selectedItem = null
            currentState.release()
        }
    }

    private inner class Release :
        State
}