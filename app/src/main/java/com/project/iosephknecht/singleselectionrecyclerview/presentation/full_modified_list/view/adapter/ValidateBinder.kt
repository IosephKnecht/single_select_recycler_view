package com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.view.adapter

import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

class ValidateBinder(
    @DrawableRes val defaultBackground: Int,
    @DrawableRes val invalidBackground: Int
) {
    fun bind(
        holder: SelectableAdapter.SelectableViewHolder,
        isValid: Boolean
    ) {
        val context = holder.itemView.context
        val backgroundRes = if (isValid) defaultBackground else invalidBackground

        with(holder) {
            customEditText?.setValueBackground(
                ContextCompat.getDrawable(context, backgroundRes)!!
            )
        }
    }
}