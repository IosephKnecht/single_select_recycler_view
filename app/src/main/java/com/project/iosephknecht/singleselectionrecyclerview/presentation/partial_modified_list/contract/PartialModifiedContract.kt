package com.project.iosephknecht.singleselectionrecyclerview.presentation.partial_modified_list.contract

import androidx.lifecycle.LiveData
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.base_selectable.contract.BaseSelectableContract
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.viewState.SelectableViewState
import java.util.*

/**
 * Contract for module with immutable items and mutable list.
 *
 * @author IosephKnecht
 */
interface PartialModifiedContract {

    interface ViewModel : BaseSelectableContract.ViewModel<UUID, SelectableViewState> {
        val confirmRemoveDialog: LiveData<SelectableViewState>

        /**
         * Add new element to list.
         */
        fun add()

        /**
         * Remove element from list.
         */
        fun remove(viewState: SelectableViewState)

        /**
         * Confirm removing from list.
         */
        fun confirmRemove()

        /**
         * Decline removing from list.
         */
        fun declineRemove()
    }
}