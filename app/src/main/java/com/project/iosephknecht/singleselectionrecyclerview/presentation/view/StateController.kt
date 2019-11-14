package com.project.iosephknecht.singleselectionrecyclerview.presentation.view

import java.util.*
import kotlin.collections.ArrayList

class StateController<T : SelectableItem>(
    items: List<T>,
    private val viewController: ViewController<T>
) {

    private var currentState: State = Unselected()
    private var selectableTuple: SelectableTuple<T>? = null

    private val mutableItems: MutableList<T> = ArrayList(items)

    fun selectItem(uuid: UUID, adapterPosition: Int) {
        currentState.select(uuid, adapterPosition)
    }

    fun add(model: T) {
        currentState.add(model)
    }

    fun confirmAdd() {
        currentState.confirmAdd()
    }

    fun remove(model: T) {
        currentState.remove(model)
    }

    fun confirmRemove() {
        currentState.confirmRemove()
    }

    fun release() {
        currentState.release()
    }

    interface ViewController<T : SelectableItem> {
        fun onSingleSelect(adapterPosition: Int)
        fun onSwapSelect(unselectAdapterPosition: Int, selectAdapterPosition: Int)
        fun onUpdate(list: List<T>, addNewElement: Boolean)
        fun onRemove(model: T)
    }

    private interface State {
        fun select(uuid: UUID, adapterPosition: Int) {}
        fun add(newItem: SelectableItem) {}
        fun confirmAdd() {}
        fun remove(removeItem: SelectableItem) {}
        fun confirmRemove() {}
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

        @Suppress("UNCHECKED_CAST")
        override fun add(newItem: SelectableItem) {
            selectableTuple = SelectableTuple(
                (newItem as T).markSelect(true),
                mutableItems.size + 1
            )

            mutableItems.add(newItem)
            viewController.onUpdate(mutableItems, true)

            currentState = ProcessAdd()
        }

        override fun remove(removeItem: SelectableItem) {
            mutableItems.find { it.uuid == removeItem.uuid }?.also { viewState ->
                viewController.onRemove(viewState)

                currentState = ProcessDelete(viewState)
            }
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

        @Suppress("UNCHECKED_CAST")
        override fun add(newItem: SelectableItem) {
            selectableTuple!!.isSelected = false

            selectableTuple = SelectableTuple(
                (newItem as T).markSelect(true),
                mutableItems.size + 1
            )

            mutableItems.add(newItem)

            viewController.onUpdate(mutableItems, true)

            currentState = ProcessAdd()
        }

        override fun remove(removeItem: SelectableItem) {
            mutableItems.find { it.uuid == removeItem.uuid }?.also { viewState ->
                if (viewState.uuid == selectableTuple!!.uuid) {
                    selectableTuple!!.isSelected = false
                }

                viewController.onRemove(viewState)

                currentState = ProcessDelete(viewState)
            }
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

        override fun confirmAdd() {
            selectableTuple!!.isSelected = false
            viewController.onUpdate(mutableItems, false)
            currentState = Unselected()
        }

        @Suppress("UNCHECKED_CAST")
        override fun remove(removeItem: SelectableItem) {
            val isSuccess = mutableItems.remove(removeItem as T)

            if (isSuccess) {
                viewController.onUpdate(mutableItems, false)
                currentState = Unselected()
            }
        }

        override fun release() {
            selectableTuple = null
            currentState = Release()
        }
    }

    private inner class ProcessDelete(
        private val removeItem: SelectableItem
    ) : State {

        // TODO: bad solve, duplicate code
        override fun select(uuid: UUID, adapterPosition: Int) {
            val selectedState = selectableTuple != null

            if (selectedState && selectableTuple!!.uuid != uuid) {
                selectableTuple!!.isSelected = false

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

                currentState = Selected()
            } else {
                mutableItems.find { it.uuid == uuid }?.also { viewState ->

                    selectableTuple = SelectableTuple(
                        viewState.markSelect(true),
                        adapterPosition
                    )

                    viewController.onSingleSelect(adapterPosition)
                }

                currentState = Selected()
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun confirmRemove() {
            val isSuccess = mutableItems.remove(removeItem as T)

            if (isSuccess) {
                viewController.onUpdate(
                    mutableItems,
                    false
                )
            }

            currentState = selectableTuple?.run {
                Selected()
            } ?: Unselected()
        }
    }

    private inner class Release : State
}