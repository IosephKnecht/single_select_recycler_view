package com.project.iosephknecht.singleselectionrecyclerview.presentation.view

import java.util.*
import kotlin.collections.ArrayList

class SingleSelectionController<T : SelectableItem>(
    items: List<T>,
    private val viewController: ViewController<T>
) {

    private val mutableItems: MutableList<T> = ArrayList(items)

    private var currentState: State<T> = Unselected()

    var currentSelectedItem: T? = null
        private set

    /* TODO: bad solve, necessary for the caller to understand whether they want to call method
    *   resetSelect() or confirmAdd() on ProcessAdd state's */
    fun isProcessAddState(): Boolean {
        return currentState is SingleSelectionController<*>.ProcessAdd
    }

    fun selectItem(uuid: UUID) {
        currentState.select(uuid)
    }

    fun addItem(newItem: T) {
        currentState.add(newItem)
    }

    fun confirmAddItem() {
        currentState.confirmAdd()
    }

    fun removeItem(preparedItemToRemove: T) {
        if (preparedItemToRemove.uuid == currentSelectedItem?.uuid) {
            currentState.currentSelectedRemove()
        } else {
            currentState.unselectedRemove(preparedItemToRemove)
        }
    }

    fun confirmRemove() {
        currentState.confirmRemove()
    }

    fun resetSelected() {
        currentState.resetSelected()
    }

    fun release() {
        currentState.release()
    }

    interface ViewController<T> {
        fun onSingleChange(uuid: UUID)
        fun onPairChange(unselectUUID: UUID, selectUUID: UUID)
        fun onUpdate(list: List<T>, addNewElement: Boolean)
        fun onRemove(viewState: T)
    }

    private interface State<T> {
        fun select(uuid: UUID) {}
        fun resetSelected() {}
        fun add(newItem: T) {}
        fun confirmAdd() {}
        fun currentSelectedRemove() {}
        fun unselectedRemove(removeItem: T) {}
        fun confirmRemove() {}
        fun release() {}
    }

    private inner class Unselected : State<T> {

        override fun select(uuid: UUID) {
            mutableItems.find { it.uuid == uuid }?.also { viewState ->
                currentSelectedItem = viewState.apply {
                    isSelected = true
                }

                viewController.onSingleChange(viewState.uuid)

                currentState = Selected()
            }
        }

        override fun add(newItem: T) {
            currentSelectedItem = newItem.apply {
                isSelected = true
            }

            mutableItems.add(newItem)

            viewController.onUpdate(mutableItems, addNewElement = true)

            currentState = ProcessAdd()
        }

        override fun unselectedRemove(removeItem: T) {
            mutableItems.find { it.uuid == removeItem.uuid }?.also { viewState ->
                viewController.onRemove(viewState)
                currentState = NotCurrentSelectedRemove(viewState)
            }
        }

        override fun release() {
            currentSelectedItem = null
            mutableItems.clear()
            currentState = Release()
        }
    }

    private inner class Selected : State<T> {

        override fun select(uuid: UUID) {
            if (currentSelectedItem!!.uuid == uuid) return

            mutableItems.find { it.uuid == uuid }?.also { viewState ->
                val unselectedUUID = currentSelectedItem!!.run {
                    isSelected = false
                    this.uuid
                }

                val selectedUUID = viewState.run {
                    isSelected = true
                    this.uuid
                }

                currentSelectedItem = viewState

                viewController.onPairChange(unselectedUUID, selectedUUID)
            }
        }

        override fun resetSelected() {
            val unselectUUID = currentSelectedItem!!.run {
                isSelected = false
                this.uuid
            }

            currentSelectedItem = null

            viewController.onSingleChange(unselectUUID)

            currentState = Unselected()
        }

        override fun add(newItem: T) {
            // TODO: raise unique error

            mutableItems.find { it.uuid == newItem.uuid }
                .takeIf { it == null }
                .also {
                    currentSelectedItem!!.isSelected = false

                    currentSelectedItem = newItem.apply {
                        isSelected = true
                    }

                    mutableItems.add(newItem)

                    viewController.onUpdate(mutableItems, addNewElement = true)

                    currentState = ProcessAdd()
                }
        }

        override fun currentSelectedRemove() {
            viewController.onRemove(currentSelectedItem!!)

            currentState = CurrentSelectedRemove()
        }

        override fun unselectedRemove(removeItem: T) {
            mutableItems.find { it.uuid == removeItem.uuid }?.also {
                viewController.onRemove(removeItem)

                currentState = NotCurrentSelectedRemove(removeItem)
            }
        }

        override fun release() {
            currentSelectedItem = null
            mutableItems.clear()
            currentState = Release()
        }
    }

    private inner class ProcessAdd : State<T> {

        override fun select(uuid: UUID) {
            if (currentSelectedItem!!.uuid == uuid) return

            mutableItems.find { it.uuid == uuid }?.also { viewState ->
                mutableItems.remove(currentSelectedItem!!.apply { isSelected = false })

                currentSelectedItem = viewState.apply {
                    isSelected = true
                }

                viewController.onUpdate(mutableItems, addNewElement = false)

                currentState = Selected()
            }
        }

        override fun confirmAdd() {
            val unselectUUID = currentSelectedItem!!.run {
                isSelected = false
                this.uuid
            }

            currentSelectedItem = null

            // FIXME: not necessary full update, need change onSingleUpdate()
            viewController.onUpdate(mutableItems, addNewElement = false)

            currentState = Unselected()
        }

        override fun currentSelectedRemove() {
            val isSuccess = mutableItems.remove(currentSelectedItem!!.apply { isSelected = false })
            currentSelectedItem = null

            if (isSuccess) {
                viewController.onUpdate(mutableItems, addNewElement = false)
            }

            currentState = Unselected()
        }

        override fun unselectedRemove(removeItem: T) {
            mutableItems.find { it.uuid == removeItem.uuid }?.also { viewState ->
                viewController.onRemove(viewState)

                currentState = NotCurrentSelectedRemove(viewState)
            }
        }

        override fun resetSelected() {
            val isSuccess = mutableItems.remove(currentSelectedItem!!.apply { isSelected = false })
            currentSelectedItem = null

            if (isSuccess) {
                viewController.onUpdate(mutableItems, addNewElement = false)
            }

            currentState = Unselected()
        }

        override fun release() {
            currentSelectedItem = null
            mutableItems.clear()
            currentState = Release()
        }
    }

    private inner class CurrentSelectedRemove : State<T> {
        override fun select(uuid: UUID) {
            mutableItems.find { it.uuid == uuid }?.also { viewState ->
                val unselectUUID = currentSelectedItem!!.run {
                    isSelected = false
                    this.uuid
                }

                val selectedUUID = viewState.run {
                    isSelected = true
                    this.uuid
                }

                currentSelectedItem = viewState

                viewController.onPairChange(unselectUUID, selectedUUID)

                currentState = Selected()
            }
        }

        override fun resetSelected() {
            val unselectUUID = currentSelectedItem!!.run {
                isSelected = false
                this.uuid
            }

            currentSelectedItem = null

            viewController.onSingleChange(unselectUUID)

            currentState = Unselected()
        }

        override fun add(newItem: T) {
            // TODO: raise unique error

            mutableItems.find { it.uuid == newItem.uuid }
                .takeIf { it == null }
                .also {
                    currentSelectedItem!!.isSelected = false

                    currentSelectedItem = newItem.apply {
                        isSelected = true
                    }

                    mutableItems.add(newItem)

                    viewController.onUpdate(mutableItems, addNewElement = true)

                    currentState = ProcessAdd()
                }
        }

        override fun confirmRemove() {
            val isSuccess = mutableItems.remove(currentSelectedItem!!.apply { isSelected = false })

            if (isSuccess) {
                viewController.onUpdate(mutableItems, addNewElement = false)
            }

            currentState = Unselected()
        }

        override fun release() {
            currentSelectedItem = null
            mutableItems.clear()
            currentState = Release()
        }
    }

    private inner class NotCurrentSelectedRemove(
        private val preparedItemToRemove: T
    ) : State<T> {

        override fun select(uuid: UUID) {
            mutableItems.find { it.uuid == uuid }?.also { viewState ->
                val unselectUUID = currentSelectedItem?.run {
                    isSelected = false
                    this.uuid
                }

                val selectedUUID = viewState.run {
                    isSelected = true
                    this.uuid
                }

                currentSelectedItem = viewState

                if (unselectUUID != null) {
                    viewController.onPairChange(unselectUUID, selectedUUID)
                } else {
                    viewController.onSingleChange(selectedUUID)
                }

                currentState = Selected()
            }
        }

        override fun resetSelected() {
            val unselectUUID = currentSelectedItem?.run {
                isSelected = false
                this.uuid
            }

            currentSelectedItem = null

            if (unselectUUID != null) viewController.onSingleChange(unselectUUID)

            currentState = Unselected()
        }

        override fun add(newItem: T) {
            mutableItems.find { it.uuid == newItem.uuid }
                .takeIf { it == null }
                .also {
                    currentSelectedItem?.isSelected = false

                    currentSelectedItem = newItem.apply {
                        isSelected = true
                    }

                    mutableItems.add(newItem)

                    viewController.onUpdate(mutableItems, addNewElement = true)

                    currentState = ProcessAdd()
                }
        }

        override fun confirmRemove() {
            val isSuccess = mutableItems.remove(preparedItemToRemove)

            if (isSuccess) {
                viewController.onUpdate(mutableItems, addNewElement = false)
            }
        }

        override fun unselectedRemove(removeItem: T) {
            mutableItems.find { it.uuid == removeItem.uuid }?.also { viewState ->
                viewController.onRemove(viewState)

                currentState = NotCurrentSelectedRemove(viewState)
            }
        }

        override fun currentSelectedRemove() {
            currentSelectedItem?.also { selectedItem ->
                viewController.onRemove(selectedItem)

                currentState = CurrentSelectedRemove()
            }
        }

        override fun release() {
            currentSelectedItem = null
            mutableItems.clear()
            currentState = Release()
        }
    }

    private inner class Release : State<T>
}