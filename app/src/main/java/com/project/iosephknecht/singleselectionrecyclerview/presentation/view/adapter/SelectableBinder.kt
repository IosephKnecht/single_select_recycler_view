package com.project.iosephknecht.singleselectionrecyclerview.presentation.view.adapter

import android.content.Context
import android.text.InputType
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.material.snackbar.Snackbar
import com.project.iosephknecht.singleselectionrecyclerview.R
import com.project.iosephknecht.singleselectionrecyclerview.presentation.viewModel.SelectableViewState

class SelectableBinder(
    @ColorRes private val selectableColor: Int,
    @ColorRes private val unselectableColor: Int,
    private val selectedTranslationZ: Float,
    private val selectableAction: (viewState: SelectableViewState) -> Unit,
    private val removeAction: (viewState: SelectableViewState) -> Unit,
    private val applyChangesAction: (viewState: SelectableViewState) -> Unit
) {
    fun bind(
        viewState: SelectableViewState,
        itemView: View,
        labelSpinner: Spinner?,
        valueTextView: EditText?,
        saveButton: ImageView?,
        removeButton: ImageView?
    ) {
        val context = itemView.context

        val backgroundColor = if (viewState.isSelected) selectableColor else unselectableColor

        val valueBackground = if (viewState.isValid)
            R.drawable.bg_edittext_border else R.drawable.bg_edittext_error

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

        itemView.translationZ = if (viewState.isSelected) selectedTranslationZ else 0f

        labelSpinner?.apply {
            isEnabled = viewState.isSelected
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

            background = ContextCompat.getDrawable(context, valueBackground)
        }

        saveButton?.setOnClickListener(saveClick)

        removeButton?.setOnClickListener(removeClick)
    }
}