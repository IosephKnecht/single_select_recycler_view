package com.project.iosephknecht.singleselectionrecyclerview.presentation.view

import java.io.Serializable

class SingleSelectionController<I : Serializable, T : SelectableItem<I>>(
    items: Collection<T>,
    private val viewController: ViewController<I, T>
) {

    private val mutableItems = LinkedHashMap<I, T>(items.associateBy { it.identifier })

    private var currentState: State<I, T> = Unselected()

    var currentSelectedItem: T? = null
        private set

    /* TODO: bad solve, necessary for the caller to understand whether they want to call method
    *   resetSelect() or confirmAdd() on ProcessAdd state's */
    fun isProcessAddState(): Boolean {
        return currentState is SingleSelectionController<*, *>.ProcessAdd
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
        if (preparedItemToRemove.identifier == currentSelectedItem?.identifier) {
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

    interface ViewController<I : Serializable, T : SelectableItem<I>> {
        fun onSingleChange(identifier: I)
        fun onPairChange(unselectIdentifier: I, selectIdentifier: I)
        fun onUpdate(list: Collection<T>, addNewElement: Boolean)
        fun onRemove(viewState: T)
    }

    private interface State<I : Serializable, T : SelectableItem<I>> {
        fun select(identifier: I) {}
        fun resetSelected() {}
        fun add(newItem: T) {}
        fun confirmAdd() {}
        fun currentSelectedRemove() {}
        fun unselectedRemove(removeItem: T) {}
        fun confirmRemove() {}
        fun release() {}
    }

    private inner class Unselected : State<I, T> {

        override fun select(identifier: I) {
            mutableItems[identifier]?.also { viewState ->
                currentSelectedItem = viewState.apply {
                    isSelected = true
                }

                viewController.onSingleChange(viewState.identifier)

                currentState = Selected()
            }
        }

        override fun add(newItem: T) {
            if (!mutableItems.containsKey(newItem.identifier)) {
                currentSelectedItem = newItem.apply {
                    isSelected = true
                }

                mutableItems[newItem.identifier] = newItem

                viewController.onUpdate(mutableItems.values, addNewElement = true)

                currentState = ProcessAdd()
            }
        }

        override fun unselectedRemove(removeItem: T) {
            mutableItems[removeItem.identifier]?.also { viewState ->
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

    private inner class Selected : State<I, T> {

        override fun select(identifier: I) {
            if (currentSelectedItem!!.identifier == identifier) return

            mutableItems[identifier]?.also { viewState ->
                val unselectedIdentifier = currentSelectedItem!!.run {
                    isSelected = false
                    this.identifier
                }

                val selectedIdentifier = viewState.run {
                    isSelected = true
                    this.identifier
                }

                currentSelectedItem = viewState

                viewController.onPairChange(unselectedIdentifier, selectedIdentifier)
            }
        }

        override fun resetSelected() {
            val unselectIdentifier = currentSelectedItem!!.run {
                isSelected = false
                this.identifier
            }

            currentSelectedItem = null

            viewController.onSingleChange(unselectIdentifier)

            currentState = Unselected()
        }

        override fun add(newItem: T) {
            if (!mutableItems.containsKey(newItem.identifier)) {
                currentSelectedItem!!.isSelected = false

                currentSelectedItem = newItem.apply {
                    isSelected = true
                }

                mutableItems[newItem.identifier] = newItem

                viewController.onUpdate(mutableItems.values, addNewElement = true)

                currentState = ProcessAdd()
            }
        }

        override fun currentSelectedRemove() {
            viewController.onRemove(currentSelectedItem!!)

            currentState = CurrentSelectedRemove()
        }

        override fun unselectedRemove(removeItem: T) {
            mutableItems[removeItem.identifier]?.also {
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

    private inner class ProcessAdd : State<I, T> {

        override fun select(identifier: I) {
            if (currentSelectedItem!!.identifier == identifier) return

            mutableItems[identifier]?.also { viewState ->
                mutableItems.remove(currentSelectedItem!!.run {
                    isSelected = false
                    this.identifier
                })

                currentSelectedItem = viewState.apply {
                    isSelected = true
                }

                viewController.onUpdate(mutableItems.values, addNewElement = false)

                currentState = Selected()
            }
        }

        override fun confirmAdd() {
            val unselectIdentifier = currentSelectedItem!!.run {
                isSelected = false
                this.identifier
            }

            currentSelectedItem = null

            // FIXME: not necessary full update, need change onSingleUpdate()
            viewController.onUpdate(mutableItems.values, addNewElement = false)

            currentState = Unselected()
        }

        override fun currentSelectedRemove() {
            val previousValue = mutableItems.remove(
                currentSelectedItem!!.run {
                    isSelected = false
                    this.identifier
                }
            )

            currentSelectedItem = null

            if (previousValue != null) {
                viewController.onUpdate(mutableItems.values, addNewElement = false)
            }

            currentState = Unselected()
        }

        override fun unselectedRemove(removeItem: T) {
            mutableItems[removeItem.identifier]?.also { viewState ->
                viewController.onRemove(viewState)

                currentState = NotCurrentSelectedRemove(viewState)
            }
        }

        override fun resetSelected() {
            val previousValue = mutableItems.remove(currentSelectedItem!!.run {
                isSelected = false
                this.identifier
            })
            currentSelectedItem = null

            if (previousValue != null) {
                viewController.onUpdate(mutableItems.values, addNewElement = false)
            }

            currentState = Unselected()
        }

        override fun release() {
            currentSelectedItem = null
            mutableItems.clear()
            currentState = Release()
        }
    }

    private inner class CurrentSelectedRemove : State<I, T> {
        override fun select(identifier: I) {
            mutableItems[identifier]?.also { viewState ->
                val unselectIdentifier = currentSelectedItem!!.run {
                    isSelected = false
                    this.identifier
                }

                val selectedIdentifier = viewState.run {
                    isSelected = true
                    this.identifier
                }

                currentSelectedItem = viewState

                viewController.onPairChange(unselectIdentifier, selectedIdentifier)

                currentState = Selected()
            }
        }

        override fun resetSelected() {
            val unselectIdentifier = currentSelectedItem!!.run {
                isSelected = false
                this.identifier
            }

            currentSelectedItem = null

            viewController.onSingleChange(unselectIdentifier)

            currentState = Unselected()
        }

        override fun add(newItem: T) {
            if (!mutableItems.containsKey(newItem.identifier)) {
                currentSelectedItem!!.isSelected = false

                currentSelectedItem = newItem.apply {
                    isSelected = true
                }

                mutableItems[newItem.identifier] = newItem

                viewController.onUpdate(mutableItems.values, addNewElement = true)

                currentState = ProcessAdd()
            }
        }

        override fun confirmRemove() {
            val previousValue = mutableItems.remove(currentSelectedItem!!.run {
                isSelected = false
                this.identifier
            })

            if (previousValue != null) {
                viewController.onUpdate(mutableItems.values, addNewElement = false)
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
    ) : State<I, T> {

        override fun select(identifier: I) {
            mutableItems[identifier]?.also { viewState ->
                val unselectIdentifier = currentSelectedItem?.run {
                    isSelected = false
                    this.identifier
                }

                val selectedIdentifier = viewState.run {
                    isSelected = true
                    this.identifier
                }

                currentSelectedItem = viewState

                if (unselectIdentifier != null) {
                    viewController.onPairChange(unselectIdentifier, selectedIdentifier)
                } else {
                    viewController.onSingleChange(selectedIdentifier)
                }

                currentState = Selected()
            }
        }

        override fun resetSelected() {
            val unselectIdentifier = currentSelectedItem?.run {
                isSelected = false
                this.identifier
            }

            currentSelectedItem = null

            if (unselectIdentifier != null) viewController.onSingleChange(unselectIdentifier)

            currentState = Unselected()
        }

        override fun add(newItem: T) {
            if (!mutableItems.containsKey(newItem.identifier)) {
                currentSelectedItem?.isSelected = false

                currentSelectedItem = newItem.apply {
                    isSelected = true
                }

                mutableItems[newItem.identifier] = newItem

                viewController.onUpdate(mutableItems.values, addNewElement = true)

                currentState = ProcessAdd()
            }
        }

        override fun confirmRemove() {
            val previousValue = mutableItems.remove(preparedItemToRemove.run { this.identifier })

            if (previousValue != null) {
                viewController.onUpdate(mutableItems.values, addNewElement = false)
            }
        }

        override fun unselectedRemove(removeItem: T) {
            mutableItems[removeItem.identifier]?.also { viewState ->
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

    private inner class Release : State<I, T>
}