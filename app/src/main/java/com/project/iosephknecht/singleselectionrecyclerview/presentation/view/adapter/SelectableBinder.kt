package com.project.iosephknecht.singleselectionrecyclerview.presentation.view.adapter

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.project.iosephknecht.singleselectionrecyclerview.presentation.view.CustomEditTextView
import com.project.iosephknecht.singleselectionrecyclerview.presentation.viewModel.SelectableViewState

class SelectableBinder(
    @ColorRes private val selectableColor: Int,
    @ColorRes private val unselectableColor: Int,
    private val selectedTranslationZ: Float,
    private val unselectedTranslationZ: Float,
    private val selectableAction: (viewState: SelectableViewState) -> Unit,
    private val removeAction: (viewState: SelectableViewState) -> Unit,
    private val applyChangesAction: (viewState: SelectableViewState) -> Unit
) {

    fun bind(
        holder: SelectableAdapter.SelectableViewHolder,
        viewState: SelectableViewState
    ) {
        val context = holder.itemView.context

        val isSelected = viewState.isSelected

        val backgroundColor = if (isSelected) selectableColor else unselectableColor

        val selectedClick = View.OnClickListener {
            selectableAction.invoke(viewState)
        }.takeIf { !isSelected }

        val saveClick = View.OnClickListener {
            applyChangesAction.invoke(viewState)
        }.takeIf { isSelected }

        val valueClick = View.OnClickListener { view ->
            view as EditText
            selectableAction.invoke(viewState)

            view.post {
                view.setSelection(view.length())
                view.requestFocus()
            }
        }.takeIf { !isSelected }

        val removeClick = View.OnClickListener {
            removeAction.invoke(viewState)
        }

        val keyboardFocusListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                val service = view.context
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

                service?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }.takeIf { isSelected }

        val transitionZValue = if (isSelected) selectedTranslationZ else unselectedTranslationZ

        with(holder) {
            itemView.apply {
                setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        backgroundColor
                    )
                )

                setOnClickListener(selectedClick)

                translationZ = transitionZValue
            }

            labelSpinner?.isEnabled = isSelected

            customEditText?.apply {
                setValueFocusable(isFocusableInTouchMode = isSelected)
                setApplyClickListener(saveClick)
                setValueClickListener(valueClick)
                setRemoveClickListener(removeClick)
                setValueFocusChangeListener(keyboardFocusListener)
            }
        }
    }
}