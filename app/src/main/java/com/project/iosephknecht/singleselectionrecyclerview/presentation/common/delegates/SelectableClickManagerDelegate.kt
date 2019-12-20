package com.project.iosephknecht.singleselectionrecyclerview.presentation.common.delegates

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.ui.CustomEditTextView
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.viewState.SelectableViewState

class SelectableClickManagerDelegate(
    private val canBeModified: Boolean,
    private val selectableAction: (viewState: SelectableViewState) -> Unit,
    private val removeAction: ((viewState: SelectableViewState) -> Unit)?,
    private val applyChangesAction: ((viewState: SelectableViewState) -> Unit)?
) : AbstractAdapterDelegate<SelectableClickManagerDelegate.ViewProvider, SelectableViewState> {

    interface ViewProvider : AbstractAdapterDelegate.BaseViewProvider {
        val customEditText: CustomEditTextView
    }

    override fun bind(viewProvider: ViewProvider, element: SelectableViewState) {
        val isSelected = element.isSelected

        val selectedClick = View.OnClickListener {
            selectableAction.invoke(element)
        }.takeIf { !isSelected }

        val saveClick = applyChangesAction?.run {
            View.OnClickListener { invoke(element) }
        }?.takeIf { isSelected }

        val valueClick = View.OnClickListener { view ->
            view as EditText
            selectableAction.invoke(element)

            view.post {
                view.setSelection(view.length())
                view.requestFocus()
            }
        }.takeIf { !isSelected }

        val removeClick = removeAction?.run {
            View.OnClickListener { invoke(element) }
        }

        val keyboardFocusListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                val service = view.context
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

                service?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }.takeIf { isSelected && canBeModified }

        with(viewProvider) {
            rootView.setOnClickListener(selectedClick)

            customEditText.apply {
                setValueFocusable(isFocusableInTouchMode = isSelected)
                setApplyClickListener(saveClick)
                setValueClickListener(valueClick)
                setRemoveClickListener(removeClick)
                setValueFocusChangeListener(keyboardFocusListener)
            }
        }
    }
}