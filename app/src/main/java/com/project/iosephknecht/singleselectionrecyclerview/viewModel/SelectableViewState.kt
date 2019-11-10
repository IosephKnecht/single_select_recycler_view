package com.project.iosephknecht.singleselectionrecyclerview.viewModel

import com.project.iosephknecht.singleselectionrecyclerview.data.SelectableModel
import java.util.*

data class SelectableViewState(
    val selectableModel: SelectableModel,
    var isSelected: Boolean = false
) {
    val uuid: UUID
        get() = selectableModel.uuid

    val label: String
        get() = selectableModel.label

    val value: String
        get() = selectableModel.value
}