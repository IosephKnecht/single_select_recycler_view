package com.project.iosephknecht.single_selection.controller

import java.io.Serializable

/**
 * Common contract for models that can be selected.
 *
 * @author IosephKnecht
 */
interface SelectableItem<T : Serializable> {
    /**
     * unique identifier for model.
     */
    val identifier: T

    /**
     * selection flag
     */
    var isSelected: Boolean
}