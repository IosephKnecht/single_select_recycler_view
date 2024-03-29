package com.project.iosephknecht.singleselectionrecyclerview.presentation.only_selection

import androidx.fragment.app.Fragment
import com.project.iosephknecht.singleselectionrecyclerview.presentation.only_selection.view.OnlySelectionFragment

/**
 * Implementation of [OnlySelectionInputModule].
 *
 * @author IosephKnecht
 */
internal class OnlySelectionInputModuleImpl : OnlySelectionInputModule {
    override fun createFragment(): Fragment {
        return OnlySelectionFragment.createInstance()
    }
}