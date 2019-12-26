package com.project.iosephknecht.singleselectionrecyclerview.presentation.cases.contract

import androidx.lifecycle.LiveData
import com.project.iosephknecht.singleselectionrecyclerview.presentation.main.model.SingleSelectionCase

/**
 * Contract for module with a list of cases.
 *
 * @author IosephKnecht
 */
interface CasesContract {

    interface ViewModel {
        val items: LiveData<List<SingleSelectionCase>>
    }
}