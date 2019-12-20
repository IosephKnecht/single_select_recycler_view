package com.project.iosephknecht.singleselectionrecyclerview.presentation.common.base_selectable.controller

import java.io.Serializable

class SingleSelectionController<I : Serializable, T : SelectableItem<I>>(
    items: Collection<T>,
    private val viewController: ViewController<I, T>
) {

    private val mutableItems = LinkedHashMap<I, T>(items.associateBy { it.identifier })

    private var currentState: State<I, T> = Unselected()

    private var currentSelectedItem: T? = null

    init {
        viewController.onFullUpdate(mutableItems.values)
    }

    fun isProcessAddState(): Boolean {
        return currentState is ProcessAdd
    }

    fun selectItem(identifier: I) {
        currentState.select(identifier)
    }

    fun addItem(newItem: T) {
        currentState.add(newItem)
    }

    fun confirmAddItem() {
        currentState.confirmAdd()
    }

    fun removeItem(preparedItemToRemove: T) {
        if (currentSelectedItem?.identifier == preparedItemToRemove.identifier) {
            currentState.removeCurrent()
        } else {
            currentState.removeNotCurrent(preparedItemToRemove.identifier)
        }
    }

    fun confirmRemove() {
        currentState.confirmRemove()
    }

    fun declineRemove() {
        currentState.declineRemove()
    }

    fun resetSelected() {
        if (isProcessAddState()) {
            currentState.declineAdd()
        } else {
            currentState.resetSelected()
        }
    }

    fun release() {
        currentState.release()
    }

    interface ViewController<I : Serializable, T : SelectableItem<I>> {
        fun onSoftUpdate(list: Collection<I>)
        fun onFullUpdate(list: Collection<T>)
        fun onAddNewElement(willBeAdded: Boolean) {}
        fun onReset(viewState: T) {}
        fun onRemove(viewState: T) {}
    }

    private interface State<I : Serializable, T : SelectableItem<I>> {
        fun select(identifier: I) {}
        fun resetSelected() {}
        fun add(newItem: T) {}
        fun confirmAdd() {}
        fun declineAdd() {}
        fun removeNotCurrent(identifier: I) {}
        fun removeCurrent() {}
        fun confirmRemove() {}
        fun declineRemove() {}
        fun release() {}
    }

    private inner class Unselected :
        State<I, T> {
        override fun select(identifier: I) {
            mutableItems[identifier]?.also { viewState ->
                currentSelectedItem = viewState.apply {
                    isSelected = true
                }

                viewController.onSoftUpdate(listOf(identifier))

                currentState = Selected()
            }
        }

        override fun add(newItem: T) {
            if (!mutableItems.containsKey(newItem.identifier)) {

                currentSelectedItem = newItem.apply {
                    isSelected = true
                }

                mutableItems[newItem.identifier] = newItem

                viewController.onAddNewElement(true)
                viewController.onFullUpdate(mutableItems.values)

                currentState = ProcessAdd()
            }
        }

        override fun removeNotCurrent(identifier: I) {
            mutableItems[identifier]?.also { viewState ->
                viewController.onRemove(viewState)
                currentState = ProcessAllUnselectedRemove(viewState)
            }
        }

        override fun release() {
            currentSelectedItem = null
            mutableItems.clear()
            currentState = Release()
        }
    }

    private inner class Selected :
        State<I, T> {
        override fun select(identifier: I) {
            if (currentSelectedItem!!.identifier == identifier) return

            mutableItems[identifier]?.also { viewState ->
                val unselectedIdentifier = currentSelectedItem!!.run {
                    isSelected = false
                    this.identifier
                }

                viewController.onReset(currentSelectedItem!!)

                val selectedIdentifier = viewState.run {
                    isSelected = true
                    this.identifier
                }

                currentSelectedItem = viewState

                viewController.onSoftUpdate(listOf(unselectedIdentifier, selectedIdentifier))
            }
        }

        override fun add(newItem: T) {
            if (!mutableItems.containsKey(newItem.identifier)) {

                currentSelectedItem!!.isSelected = false

                currentSelectedItem = newItem.apply {
                    isSelected = true
                }

                mutableItems[newItem.identifier] = newItem

                viewController.onAddNewElement(true)
                viewController.onFullUpdate(mutableItems.values)

                currentState = ProcessAdd()
            }
        }

        override fun removeCurrent() {
            viewController.onRemove(currentSelectedItem!!)
            currentState = ProcessSelectedRemove()
        }

        override fun removeNotCurrent(identifier: I) {
            mutableItems[identifier]?.also { viewState ->
                viewController.onRemove(viewState)

                currentState = ProcessUnselectedRemove(viewState)
            }
        }

        override fun resetSelected() {
            val unselectIdentifier = currentSelectedItem!!.run {
                isSelected = false
                this.identifier
            }

            viewController.onReset(currentSelectedItem!!)

            currentSelectedItem = null

            viewController.onSoftUpdate(listOf(unselectIdentifier))

            currentState = Unselected()
        }

        override fun release() {
            currentSelectedItem = null
            mutableItems.clear()
            currentState = Release()
        }
    }

    private inner class ProcessAdd :
        State<I, T> {

        override fun select(identifier: I) {
            if (currentSelectedItem!!.identifier == identifier) return

            mutableItems[identifier]?.also { viewState ->
                val previousItem = mutableItems.remove(currentSelectedItem!!.run {
                    isSelected = false
                    this.identifier
                })

                val selectedIdentifier = viewState.run {
                    isSelected = true
                    this.identifier
                }

                currentSelectedItem = viewState

                if (previousItem != null) {
                    viewController.onAddNewElement(false)
                    viewController.onFullUpdate(mutableItems.values)
                } else {
                    viewController.onSoftUpdate(listOf(selectedIdentifier))
                }

                currentState = Selected()
            }
        }

        override fun removeNotCurrent(identifier: I) {
            mutableItems[identifier]?.also { viewState ->
                viewController.onRemove(viewState)
                currentState = ProcessUnselectedAddedRemove(viewState)
            }
        }

        override fun removeCurrent() {
            val previousValue = mutableItems.remove(
                currentSelectedItem!!.run {
                    isSelected = false
                    this.identifier
                }
            )

            currentSelectedItem = null

            if (previousValue != null) {
                viewController.onAddNewElement(false)
                viewController.onFullUpdate(mutableItems.values)
            }

            currentState = Unselected()
        }

        override fun confirmAdd() {
            val unselectIdentifier = currentSelectedItem!!.run {
                isSelected = false
                this.identifier
            }

            currentSelectedItem = null

            viewController.onAddNewElement(false)
            viewController.onSoftUpdate(listOf(unselectIdentifier))

            currentState = Unselected()
        }

        override fun declineAdd() {
            val previousValue = mutableItems.remove(currentSelectedItem!!.run {
                isSelected = false
                this.identifier
            })
            currentSelectedItem = null

            if (previousValue != null) {
                viewController.onAddNewElement(false)
                viewController.onFullUpdate(mutableItems.values)
            }

            currentState = Unselected()
        }

        override fun release() {
            currentSelectedItem = null
            mutableItems.clear()
            currentState = Release()
        }
    }

    private inner class ProcessAllUnselectedRemove(
        private val preparedItemToRemove: T
    ) : State<I, T> {

        override fun confirmRemove() {
            val previousValue = mutableItems.remove(preparedItemToRemove.run { this.identifier })

            if (previousValue != null) {
                viewController.onAddNewElement(false)
                viewController.onFullUpdate(mutableItems.values)
            }

            currentState = Unselected()
        }

        override fun declineRemove() {
            currentState = Unselected()
        }

        override fun release() {
            currentSelectedItem = null
            mutableItems.clear()
            currentState = Release()
        }
    }

    private inner class ProcessUnselectedRemove(
        private val preparedItemToRemove: T
    ) : State<I, T> {

        override fun confirmRemove() {
            val previousValue = mutableItems.remove(preparedItemToRemove.run { this.identifier })

            if (previousValue != null) {
                viewController.onAddNewElement(false)
                viewController.onFullUpdate(mutableItems.values)
            }

            currentState = Selected()
        }

        override fun declineRemove() {
            currentState = Selected()
        }

        override fun release() {
            currentSelectedItem = null
            mutableItems.clear()
            currentState = Release()
        }
    }

    private inner class ProcessSelectedRemove :
        State<I, T> {

        override fun confirmRemove() {
            val previousValue = mutableItems.remove(currentSelectedItem!!.run { this.identifier })
            currentSelectedItem = null

            if (previousValue != null) {
                viewController.onAddNewElement(false)
                viewController.onFullUpdate(mutableItems.values)
            }

            currentState = Unselected()
        }

        override fun declineRemove() {
            currentState = Selected()
        }

        override fun release() {
            currentSelectedItem = null
            mutableItems.clear()
            currentState = Release()
        }
    }

    private inner class ProcessUnselectedAddedRemove(
        private val preparedItemToRemove: T
    ) : State<I, T> {

        override fun confirmRemove() {
            val previousValue = mutableItems.remove(preparedItemToRemove.run { this.identifier })

            if (previousValue != null) {
                viewController.onAddNewElement(false)
                viewController.onFullUpdate(mutableItems.values)
            }

            currentState = ProcessAdd()
        }

        override fun declineRemove() {
            currentState = ProcessAdd()
        }

        override fun release() {
            currentSelectedItem = null
            mutableItems.clear()
            currentState = Release()
        }
    }

    private inner class Release :
        State<I, T>
}
