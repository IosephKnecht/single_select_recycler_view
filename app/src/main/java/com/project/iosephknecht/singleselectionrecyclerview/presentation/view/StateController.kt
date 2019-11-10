package com.project.iosephknecht.singleselectionrecyclerview.presentation.view

import java.util.*
import kotlin.collections.ArrayList

class StateController<T : SelectableItem>(
    items: List<T>,
    private val creator: () -> T,
    private val viewController: ViewController<T>
) {

    private var currentState: State = Unselected()
    private var selectedItem: T? = null

    private val mutableItems: MutableList<T> = ArrayList(items)

    fun selectItem(uuid: UUID) {
        currentState.select(uuid)
    }

    fun add() {
        currentState.add()
    }

    fun confirmSave() {
        currentState.confirmSave()
    }

    fun release() {
        currentState.release()
    }

    interface ViewController<T : SelectableItem> {
        fun onUpdate(list: List<T>, addNewElement: Boolean)
    }

    private interface State {
        fun select(uuid: UUID) {}
        fun add() {}
        fun confirmSave() {}
        fun release() {}
    }

    private inner class Unselected : State {
        override fun select(uuid: UUID) {
            mutableItems.find { it.uuid == uuid }?.also { viewState ->
                selectedItem = viewState.apply {
                    isSelected = true
                }

                viewController.onUpdate(mutableItems, false)

                currentState = Selected()
            }
        }

        override fun add() {
            val newItem = creator.invoke().apply {
                isSelected = true
            }

            selectedItem = newItem
            mutableItems.add(newItem)
            viewController.onUpdate(mutableItems, true)

            currentState = ProcessAdd()
        }

        override fun release() {
            selectedItem = null
            currentState = Release()
        }
    }

    private inner class Selected : State {
        override fun select(uuid: UUID) {
            if (selectedItem!!.uuid == uuid) return

            mutableItems.find { it.uuid == uuid }?.also { viewState ->
                selectedItem!!.isSelected = false
                selectedItem = viewState.apply {
                    isSelected = true
                }

                viewController.onUpdate(mutableItems, false)
            }
        }

        override fun add() {
            val newItem = creator.invoke().apply {
                isSelected = true
            }

            selectedItem!!.isSelected = false
            selectedItem = newItem

            mutableItems.add(newItem)

            viewController.onUpdate(mutableItems, true)

            currentState = ProcessAdd()
        }

        override fun release() {
            selectedItem = null
            currentState = Release()
        }
    }

    private inner class ProcessAdd : State {
        override fun select(uuid: UUID) {
            mutableItems.find { it.uuid == uuid }?.also { viewState ->
                mutableItems.remove(selectedItem!!)

                selectedItem = viewState.apply {
                    isSelected = true
                }

                viewController.onUpdate(mutableItems, false)

                currentState = Selected()
            }
        }

        override fun confirmSave() {
            selectedItem!!.isSelected = false
            viewController.onUpdate(mutableItems, false)
            currentState = Unselected()
        }

        override fun release() {
            selectedItem = null
            currentState = Release()
        }
    }

    private inner class Release : State
}