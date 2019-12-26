package com.project.iosephknecht.single_selection.controller

import java.io.Serializable

/**
 * Contract for listener [SingleSelectionController].
 *
 * @author IosephKnecht
 */
interface ViewController<I : Serializable, T : SelectableItem<I>> {
    /**
     * Callback to update some items.
     */
    fun onSoftUpdate(list: Collection<I>)

    /**
     * Callback to update all items.
     */
    fun onFullUpdate(list: Collection<T>)

    /**
     * Callback to notify about state of the add mode.
     *
     * @param willBeAdded true - if at the moment a new item is in list.
     */
    fun onAddNewElement(willBeAdded: Boolean) {}

    /**
     * Callback to reset intermediate model states.
     */
    fun onReset(viewState: T) {}

    /**
     * Callback called after declaration of intent to delete an item.
     */
    fun onRemove(viewState: T) {}
}