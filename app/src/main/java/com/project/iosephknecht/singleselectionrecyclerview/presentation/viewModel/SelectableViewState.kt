package com.project.iosephknecht.singleselectionrecyclerview.presentation.viewModel

import com.project.iosephknecht.singleselectionrecyclerview.data.SomeCategory
import com.project.iosephknecht.singleselectionrecyclerview.data.SomeModel
import com.project.iosephknecht.singleselectionrecyclerview.presentation.view.SelectableItem
import java.util.*

data class SelectableViewState(
    var someModel: SomeModel,
    override var isSelected: Boolean = false
) : SelectableItem {

    override val uuid: UUID
        get() = someModel.uuid

    val originalLabel: SomeCategory
        get() = someModel.label

    val originalValue: CharSequence
        get() = someModel.value

    var changedLabel: SomeCategory? = null

    var changedValue: CharSequence? = null

    fun hasChanges(): Boolean {
        return changedValue != null || changedLabel != null
    }
}