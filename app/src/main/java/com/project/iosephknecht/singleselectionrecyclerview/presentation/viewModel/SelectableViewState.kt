package com.project.iosephknecht.singleselectionrecyclerview.presentation.viewModel

import com.project.iosephknecht.singleselectionrecyclerview.data.SomeModel
import com.project.iosephknecht.singleselectionrecyclerview.presentation.view.SelectableItem
import java.util.*

data class SelectableViewState(
    val someModel: SomeModel,
    override var isSelected: Boolean = false
) : SelectableItem {

    override val uuid: UUID
        get() = someModel.uuid

    val label: String
        get() = someModel.label

    val value: String
        get() = someModel.value
}