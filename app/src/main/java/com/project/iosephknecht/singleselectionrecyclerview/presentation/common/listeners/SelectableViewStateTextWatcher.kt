package com.project.iosephknecht.singleselectionrecyclerview.presentation.common.listeners

import android.text.Editable
import android.text.TextWatcher
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.viewState.SelectableViewState

class SelectableViewStateTextWatcher(
    private val block: SelectableViewState.(value: CharSequence?) -> Unit
) : TextWatcher {

    var viewState: SelectableViewState? = null

    override fun afterTextChanged(s: Editable?) {
        viewState?.also { block.invoke(it, s) }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // ignore
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // ignore
    }
}