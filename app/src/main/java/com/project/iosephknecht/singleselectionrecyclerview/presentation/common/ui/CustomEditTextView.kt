package com.project.iosephknecht.singleselectionrecyclerview.presentation.common.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.project.iosephknecht.singleselectionrecyclerview.R

/**
 * Custom editable field implementation with multi-state support.
 *
 * @author IosephKnecht
 */
internal class CustomEditTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    /**
     * State of view.
     *
     * @param inputType value of [InputType]
     * @param applyBtnVisible visibility for apply button.
     * @param removeBtnVisible visibility for remove button.
     */
    enum class State(
        val inputType: Int,
        val applyBtnVisible: Int,
        val removeBtnVisible: Int
    ) {
        /**
         * May be editable.
         */
        EDITABLE(
            inputType = InputType.TYPE_CLASS_TEXT,
            applyBtnVisible = View.VISIBLE,
            removeBtnVisible = View.VISIBLE
        ),
        /**
         * Only non-editable text field.
         */
        ONLY_READABLE(
            inputType = InputType.TYPE_NULL,
            applyBtnVisible = View.GONE,
            removeBtnVisible = View.GONE
        ),
        /**
         * Only non-editable text field and remove button.
         */
        READABLE_WITH_REMOVE(
            inputType = InputType.TYPE_NULL,
            applyBtnVisible = View.GONE,
            removeBtnVisible = View.VISIBLE
        ),
        /**
         * Only non-editable text field and progress bar.
         */
        LOADING(
            inputType = InputType.TYPE_NULL,
            applyBtnVisible = View.GONE,
            removeBtnVisible = View.GONE
        );
    }

    private val valueEditText: EditText
    private val applyButton: ImageView
    private val removeButton: ImageView
    private val progress: ProgressBar

    private var currentState: State? = null

    init {
        val view = LayoutInflater.from(context).inflate(
            R.layout.custom_edit_text_layout,
            this,
            true
        )

        valueEditText = view.findViewById(R.id.value)
        applyButton = view.findViewById(R.id.save_button)
        removeButton = view.findViewById(R.id.delete_button)
        progress = view.findViewById(R.id.progress)
    }

    fun setState(state: State) {
        if (state != currentState) {
            applyState(state)
            currentState = state
        }
    }

    fun setValueFocusable(
        isFocusable: Boolean? = null,
        isFocusableInTouchMode: Boolean? = null
    ) {
        isFocusable?.also { valueEditText.isFocusable = it }
        isFocusableInTouchMode?.also { valueEditText.isFocusableInTouchMode = it }
    }

    fun setText(value: CharSequence?) {
        valueEditText.setText(value)
    }

    fun addValueTextWatcher(watcher: TextWatcher) {
        valueEditText.addTextChangedListener(watcher)
    }

    fun removeValueTextWatcher(watcher: TextWatcher) {
        valueEditText.removeTextChangedListener(watcher)
    }

    fun setValueFocusChangeListener(listener: OnFocusChangeListener?) {
        valueEditText.onFocusChangeListener = listener
    }

    fun setValueClickListener(listener: OnClickListener?) {
        valueEditText.setOnClickListener(listener)
    }

    fun setValueBackground(drawable: Drawable) {
        valueEditText.background = drawable
    }

    fun setApplyClickListener(listener: OnClickListener?) {
        applyButton.setOnClickListener(listener)
    }

    fun setRemoveClickListener(listener: OnClickListener?) {
        removeButton.setOnClickListener(listener)
    }

    private fun applyState(state: State) {
        with(state) {
            valueEditText.inputType = inputType
            applyButton.visibility = applyBtnVisible
            removeButton.visibility = removeBtnVisible
            progress.visibility = if (state == State.LOADING) View.VISIBLE else View.GONE
        }
    }
}