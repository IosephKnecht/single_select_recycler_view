package com.project.iosephknecht.singleselectionrecyclerview.presentation.view.adapter

import android.content.Context
import android.text.InputType
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.project.iosephknecht.singleselectionrecyclerview.presentation.viewModel.SelectableViewState

class SelectableBinder(
    @ColorRes private val selectableColor: Int,
    @ColorRes private val unselectableColor: Int,
    private val selectableAction: (viewState: SelectableViewState) -> Unit,
    private val removeAction: (viewState: SelectableViewState) -> Unit,
    private val applyChangesAction: (viewState: SelectableViewState) -> Unit
) {
    fun bind(
        viewState: SelectableViewState,
        itemView: View,
        labelTextView: TextView?,
        valueTextView: EditText?,
        saveButton: ImageView?,
        removeButton: ImageView?
    ) {
        val context = itemView.context

        val backgroundColor = if (viewState.isSelected) selectableColor else unselectableColor
        val inputType = if (viewState.isSelected) InputType.TYPE_CLASS_TEXT else InputType.TYPE_NULL

        val selectedClick = View.OnClickListener {
            selectableAction.invoke(viewState)
        }.takeIf { !viewState.isSelected }

        val saveClick = View.OnClickListener {
            applyChangesAction.invoke(viewState)
        }.takeIf { viewState.isSelected }

        val valueClick = View.OnClickListener { view ->
            view as EditText
            view.requestFocus()
            selectableAction.invoke(viewState)

            view.post {
                view.setSelection(view.length())
                view.requestFocus()
            }
        }.takeIf { !viewState.isSelected }

        val removeClick = View.OnClickListener {
            removeAction.invoke(viewState)
        }

        val keyboardFocusListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                val service = view.context
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

                service?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }.takeIf { viewState.isSelected }

        itemView.setBackgroundColor(
            ContextCompat.getColor(
                context,
                backgroundColor
            )
        )

        itemView.setOnClickListener(selectedClick)

        labelTextView?.apply {
            setOnClickListener(selectedClick)
        }

        valueTextView?.apply {
            onFocusChangeListener = keyboardFocusListener
            isFocusableInTouchMode = viewState.isSelected

            if (!viewState.isSelected) {
                post {
                    clearFocus()
                }
            }

            setInputType(inputType)
            setOnClickListener(valueClick)
        }

        saveButton?.setOnClickListener(saveClick)

        removeButton?.setOnClickListener(removeClick)
    }
}