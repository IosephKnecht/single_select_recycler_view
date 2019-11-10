package com.project.iosephknecht.singleselectionrecyclerview.presentation.view

import java.util.*

interface SelectableItem {
    val uuid: UUID
    var isSelected: Boolean
}