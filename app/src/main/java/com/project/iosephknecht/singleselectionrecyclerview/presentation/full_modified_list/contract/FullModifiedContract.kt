package com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.contract

import androidx.lifecycle.LiveData
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.base_selectable.contract.BaseSelectableContract
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.viewState.SelectableViewState
import java.util.*

/**
 * Contract for module with full modified list items.
 *
 * @author IosephKnecht
 */
interface FullModifiedContract {
    interface ViewModel : BaseSelectableContract.ViewModel<UUID, SelectableViewState> {
        val addState: LiveData<Boolean>
        val confirmRemoveDialog: LiveData<SelectableViewState>

        /**
         * Add new element to list.
         */
        fun add()

        /**
         * Confirm adding to list.
         */
        fun confirmAdd()

        /**
         * Remove element from list.
         */
        fun remove(selectableViewState: SelectableViewState)

        /**
         * Confirm removing from list.
         */
        fun confirmRemove()

        /**
         * Decline removing from list.
         */
        fun declineRemove()

        /**
         * Apply change to element of list.
         */
        fun applyChanges(viewState: SelectableViewState)
    }
}