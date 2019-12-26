package com.project.iosephknecht.singleselectionrecyclerview.presentation.partial_modified_list

import androidx.fragment.app.Fragment
import com.project.iosephknecht.singleselectionrecyclerview.presentation.partial_modified_list.view.PartialModifiedFragment

/**
 * Implementation of [PartialModifiedInputModuleContract].
 *
 * @author IosephKnecht
 */
internal class PartialModifiedInputModule : PartialModifiedInputModuleContract {
    override fun createFragment(): Fragment {
        return PartialModifiedFragment.createInstance()
    }
}