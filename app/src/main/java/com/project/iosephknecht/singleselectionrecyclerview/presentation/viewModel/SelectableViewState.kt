package com.project.iosephknecht.singleselectionrecyclerview.presentation.viewModel

import com.project.iosephknecht.singleselectionrecyclerview.data.SomeCategory
import com.project.iosephknecht.singleselectionrecyclerview.data.SomeModel
import com.project.iosephknecht.singleselectionrecyclerview.presentation.view.SelectableItem
import java.util.*

data class SelectableViewState(
    var someModel: SomeModel,
    var isValid: Boolean = true,
    override var isSelected: Boolean = false
) : SelectableItem {

    override val uuid: UUID
        get() = someModel.uuid

    var changedLabel: SomeCategory = someModel.label

    var changedValue: CharSequence = someModel.value

    fun hasChanges(): Boolean {
        return changedValue != someModel.value || changedLabel != someModel.label
    }

    fun reset(isValid: Boolean = true) {
        changedLabel = someModel.label
        changedValue = someModel.value
        this.isValid = isValid
    }

    fun applyChanges(someModel: SomeModel, isValid: Boolean = true) {
        this.someModel = someModel
        this.isValid = isValid
        this.changedValue = someModel.value
        this.changedLabel = someModel.label
    }
}