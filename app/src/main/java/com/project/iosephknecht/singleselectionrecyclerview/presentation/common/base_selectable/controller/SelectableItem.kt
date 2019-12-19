package com.project.iosephknecht.singleselectionrecyclerview.presentation.common.base_selectable.controller

import java.io.Serializable

interface SelectableItem<T : Serializable> {
    val identifier: T
    var isSelected: Boolean
}