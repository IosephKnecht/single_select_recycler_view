package com.project.iosephknecht.singleselectionrecyclerview.presentation

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

internal fun inflate(
    parent: ViewGroup,
    @LayoutRes layoutId: Int,
    attachToRoot: Boolean = false
): View {
    return LayoutInflater.from(parent.context).inflate(layoutId, parent, attachToRoot)
}

internal inline fun <reified T> Fragment.requestApplicationAs(): T {
    return requireContext().applicationContext as T
}

internal inline fun <reified T> Activity.requestApplicationAs(): T {
    return application.applicationContext as T
}

internal fun RecyclerView.disableItemChangeAnimation() {
    itemAnimator?.apply {
        require(this is SimpleItemAnimator)
        supportsChangeAnimations = false
    }
}