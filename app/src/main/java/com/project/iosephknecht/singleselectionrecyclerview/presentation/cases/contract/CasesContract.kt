package com.project.iosephknecht.singleselectionrecyclerview.presentation.cases.contract

import androidx.lifecycle.LiveData
import com.project.iosephknecht.singleselectionrecyclerview.presentation.main.model.SingleSelectionCase

interface CasesContract {

    interface ViewModel {
        val items: LiveData<List<SingleSelectionCase>>
    }
}