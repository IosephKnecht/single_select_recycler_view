package com.project.iosephknecht.single_selection.controller

import java.io.Serializable

/**
 * Common contract for models that can be selected.
 *
 * @param identifier unique identifier.
 * @param isSelected selection flag.
 *
 * @author IosephKnecht
 */
interface SelectableItem<T : Serializable> {
    val identifier: T
    var isSelected: Boolean
}