package com.project.iosephknecht.singleselectionrecyclerview.presentation.common.viewState

import com.project.iosephknecht.singleselectionrecyclerview.data.SomeCategory
import com.project.iosephknecht.singleselectionrecyclerview.data.SomeModel
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.base_selectable.controller.SelectableItem
import java.util.*

data class SelectableViewState(
    var someModel: SomeModel,
    val isEditableValue: Boolean,
    val isEditableLabel: Boolean,
    var isValid: Boolean = true,
    var isLoading: Boolean = false,
    override var isSelected: Boolean = false
) : SelectableItem<UUID> {

    override val identifier: UUID
        get() = someModel.uuid

    var changedLabel: SomeCategory = someModel.label

    var changedValue: CharSequence = someModel.value

    fun hasChanges(): Boolean {
        return changedValue != someModel.value || changedLabel != someModel.label
    }

    fun reset(
        changedLabel: SomeCategory,
        changedValue: CharSequence,
        isValid: Boolean = true,
        isLoading: Boolean = false
    ) {
        this.changedLabel = changedLabel
        this.changedValue = changedValue
        this.isValid = isValid
        this.isLoading = isLoading
    }

    fun applyChanges(
        someModel: SomeModel,
        isValid: Boolean = true,
        isLoading: Boolean = false
    ) {
        this.someModel = someModel
        this.isValid = isValid
        this.isLoading = isLoading
        this.changedValue = someModel.value
        this.changedLabel = someModel.label
    }
}