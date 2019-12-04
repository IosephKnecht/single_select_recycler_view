package com.project.iosephknecht.singleselectionrecyclerview.presentation.view

import java.io.Serializable

interface SelectableItem<T : Serializable> {
    val identifier: T
    var isSelected: Boolean
}