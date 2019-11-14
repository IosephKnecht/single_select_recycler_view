package com.project.iosephknecht.singleselectionrecyclerview.presentation.view

import java.util.*
import kotlin.collections.ArrayList

class StateController<T : SelectableItem>(
    items: List<T>,
    private val creator: () -> T,
    private val viewController: ViewController<T>
) {

    private var currentState: State = Unselected()
    private var selectableTuple: SelectableTuple<T>? = null

    private val mutableItems: MutableList<T> = ArrayList(items)

    fun selectItem(uuid: UUID, adapterPosition: Int) {
        currentState.select(uuid, adapterPosition)
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
        fun onSingleSelect(adapterPosition: Int)
        fun onSwapSelect(unselectAdapterPosition: Int, selectAdapterPosition: Int)
        fun onUpdate(list: List<T>, addNewElement: Boolean)
    }

    private interface State {
        fun select(uuid: UUID, adapterPosition: Int) {}
        fun add() {}
        fun confirmSave() {}
        fun release() {}
    }

    private fun T.markSelect(isSelected: Boolean): T {
        return this.apply { this.isSelected = isSelected }
    }

    private data class SelectableTuple<T : SelectableItem>(
        val model: T,
        val adapterPosition: Int
    ) {
        val uuid: UUID
            get() = model.uuid

        var isSelected: Boolean
            get() = model.isSelected
            set(value) {
                model.isSelected = value
            }
    }

    private inner class Unselected : State {
        override fun select(uuid: UUID, adapterPosition: Int) {
            mutableItems.find { it.uuid == uuid }?.also { viewState ->
                selectableTuple = SelectableTuple(
                    viewState.markSelect(true),
                    adapterPosition
                )

                viewController.onSingleSelect(adapterPosition)

                currentState = Selected()
            }
        }

        override fun add() {
            val newItem = creator.invoke().apply {
                isSelected = true
            }

            selectableTuple = SelectableTuple(
                newItem,
                mutableItems.size + 1
            )

            mutableItems.add(newItem)
            viewController.onUpdate(mutableItems, true)

            currentState = ProcessAdd()
        }

        override fun release() {
            selectableTuple = null
            currentState = Release()
        }
    }

    private inner class Selected : State {
        override fun select(uuid: UUID, adapterPosition: Int) {
            if (selectableTuple!!.uuid == uuid) return

            mutableItems.find { it.uuid == uuid }?.also { viewState ->
                selectableTuple!!.isSelected = false

                val unselectPosition = selectableTuple!!.adapterPosition

                selectableTuple = SelectableTuple(
                    viewState.markSelect(true),
                    adapterPosition
                )

                viewController.onSwapSelect(
                    unselectAdapterPosition = unselectPosition,
                    selectAdapterPosition = adapterPosition
                )
            }
        }

        override fun add() {
            val newItem = creator.invoke().apply {
                isSelected = true
            }

            selectableTuple!!.isSelected = false
            selectableTuple = SelectableTuple(
                newItem,
                mutableItems.size + 1
            )

            mutableItems.add(newItem)

            viewController.onUpdate(mutableItems, true)

            currentState = ProcessAdd()
        }

        override fun release() {
            selectableTuple = null
            currentState = Release()
        }
    }

    private inner class ProcessAdd : State {
        override fun select(uuid: UUID, adapterPosition: Int) {
            mutableItems.find { it.uuid == uuid }?.also { viewState ->
                mutableItems.remove(selectableTuple!!.model)

                selectableTuple = SelectableTuple(
                    viewState.apply { isSelected = true },
                    adapterPosition
                )

                viewController.onUpdate(mutableItems, false)

                currentState = Selected()
            }
        }

        override fun confirmSave() {
            selectableTuple!!.isSelected = false
            viewController.onUpdate(mutableItems, false)
            currentState = Unselected()
        }

        override fun release() {
            selectableTuple = null
            currentState = Release()
        }
    }

    private inner class Release : State
}