package com.project.iosephknecht.singleselectionrecyclerview.presentation

import android.app.Activity
import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.SerialDisposable

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

internal fun Disposable.set(serialDisposable: SerialDisposable) {
    serialDisposable.set(this)
}

internal fun RecyclerView.scrollToLastPosition() {
    adapter?.also {
        val position = it.itemCount

        if (position > 0) {
            scrollToPosition(position - 1)
        }
    }
}

internal fun View.visible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

internal fun Resources.getFloatDimension(@DimenRes resId: Int): Float {
    val outValue = TypedValue()
    getValue(resId, outValue, true)
    return outValue.float
}