package com.project.iosephknecht.singleselectionrecyclerview.presentation.cases.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.iosephknecht.singleselectionrecyclerview.presentation.cases.contract.CasesContract
import com.project.iosephknecht.singleselectionrecyclerview.presentation.main.model.SingleSelectionCase

/**
 * Implementation [CasesContract.ViewModel].
 *
 * @author aa.mezencev
 */
class CasesViewModel : ViewModel(), CasesContract.ViewModel {

    override val items = MutableLiveData<List<SingleSelectionCase>>()

    init {
        items.value = buildCaseList()
    }

    private fun buildCaseList() = SingleSelectionCase.values().toList()
}