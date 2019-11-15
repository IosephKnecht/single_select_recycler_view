package com.project.iosephknecht.singleselectionrecyclerview.presentation.view

import java.util.*
import kotlin.collections.ArrayList

class StateController<T : SelectableItem>(
    items: List<T>,
    private val viewController: ViewController<T>
) {

    private var currentState: State = Unselected()

    var selectableItem: T? = null
        private set

    private val mutableItems: MutableList<T> = ArrayList(items)

    /* TODO: bad solve, necessary for the caller to understand whether they want to call method
    *   resetSelect() or confirmAdd() on ProcessAdd state's */
    fun needAddConfirm(): Boolean {
        return currentState.run { this as? StateController<*>.ProcessAdd } != null
    }

    fun selectItem(uuid: UUID) {
        currentState.select(uuid)
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

    fun resetSelect() {
        currentState.resetSelected()
    }

    fun release() {
        currentState.release()
    }

    interface ViewController<T : SelectableItem> {
        fun onSingleChange(uuid: UUID)
        fun onPairChange(unselectUUID: UUID, selectUUID: UUID)
        fun onUpdate(list: List<T>, addNewElement: Boolean)
        fun onRemove(model: T)
    }

    private interface State {
        fun select(uuid: UUID) {}
        fun resetSelected() {}
        fun add(newItem: SelectableItem) {}
        fun confirmAdd() {}
        fun remove(removeItem: SelectableItem) {}
        fun confirmRemove() {}
        fun release() {}
    }

    private inner class Unselected : State {
        override fun select(uuid: UUID) {
            mutableItems.find { it.uuid == uuid }?.also { viewState ->
                selectableItem = viewState.apply {
                    isSelected = true
                }

                viewController.onSingleChange(selectableItem!!.uuid)

                currentState = Selected()
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun add(newItem: SelectableItem) {
            selectableItem = newItem.apply {
                isSelected = true
            } as T

            mutableItems.add(newItem as T)
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
            selectableItem = null
            currentState = Release()
        }
    }

    private inner class Selected : State {
        override fun select(uuid: UUID) {
            if (selectableItem!!.uuid == uuid) return

            mutableItems.find { it.uuid == uuid }?.also { viewState ->
                val unselectedUUID = selectableItem!!.run {
                    isSelected = false
                    return@run this.uuid
                }

                selectableItem = viewState.apply {
                    isSelected = true
                }

                viewController.onPairChange(
                    unselectUUID = unselectedUUID,
                    selectUUID = viewState.uuid
                )
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun add(newItem: SelectableItem) {
            selectableItem!!.isSelected = false

            selectableItem = newItem as T

            mutableItems.add(
                newItem.apply { isSelected = true }
            )

            viewController.onUpdate(mutableItems, true)

            currentState = ProcessAdd()
        }

        override fun remove(removeItem: SelectableItem) {
            mutableItems.find { it.uuid == removeItem.uuid }?.also { viewState ->
                if (viewState.uuid == selectableItem!!.uuid) {
                    selectableItem!!.isSelected = false
                }

                viewController.onRemove(viewState)

                currentState = ProcessDelete(viewState)
            }
        }

        override fun resetSelected() {
            val uuid = selectableItem!!.run {
                isSelected = false
                return@run this.uuid
            }

            selectableItem = null
            viewController.onSingleChange(uuid)

            currentState = Unselected()
        }

        override fun release() {
            selectableItem = null
            currentState = Release()
        }
    }

    private inner class ProcessAdd : State {
        override fun select(uuid: UUID) {
            mutableItems.find { it.uuid == uuid }?.also { viewState ->
                mutableItems.remove(selectableItem!!)

                selectableItem = viewState.apply {
                    isSelected = true
                }

                viewController.onUpdate(mutableItems, false)

                currentState = Selected()
            }
        }

        override fun confirmAdd() {
            selectableItem!!.isSelected = false
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

        override fun resetSelected() {
            mutableItems.remove(selectableItem!!)

            selectableItem!!.isSelected = false
            selectableItem = null

            viewController.onUpdate(mutableItems, false)

            currentState = Unselected()
        }

        override fun release() {
            selectableItem = null
            currentState = Release()
        }
    }

    private inner class ProcessDelete(
        private val removeItem: SelectableItem
    ) : State {

        // TODO: bad solve, duplicate code
        override fun select(uuid: UUID) {
            val selectedState = selectableItem != null

            if (selectedState && selectableItem!!.uuid != uuid) {
                selectableItem!!.isSelected = false

                mutableItems.find { it.uuid == uuid }?.also { viewState ->
                    val unselectUUID = selectableItem!!.run {
                        isSelected = false
                        return@run this.uuid
                    }

                    selectableItem = viewState.apply {
                        isSelected = true
                    }

                    viewController.onPairChange(
                        unselectUUID = unselectUUID,
                        selectUUID = viewState.uuid
                    )
                }

                currentState = Selected()
            } else {
                mutableItems.find { it.uuid == uuid }?.also { viewState ->

                    selectableItem = viewState.apply {
                        isSelected = true
                    }

                    viewController.onSingleChange(viewState.uuid)
                }

                currentState = Selected()
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun add(newItem: SelectableItem) {
            selectableItem?.isSelected = false

            selectableItem = newItem.apply {
                isSelected = true
            } as T

            mutableItems.add(newItem as T)
            viewController.onUpdate(mutableItems, true)

            currentState = ProcessAdd()
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

            currentState = selectableItem?.run {
                Selected()
            } ?: Unselected()
        }

        override fun resetSelected() {
            selectableItem?.takeIf { it.uuid == removeItem.uuid }
                ?.also { viewState ->
                    val uuid = viewState.run {
                        isSelected = false
                        return@run this.uuid
                    }

                    viewController.onSingleChange(uuid)

                    currentState = Unselected()
                }
        }

        override fun release() {
            selectableItem = null
            currentState = Release()
        }
    }

    private inner class Release : State
}