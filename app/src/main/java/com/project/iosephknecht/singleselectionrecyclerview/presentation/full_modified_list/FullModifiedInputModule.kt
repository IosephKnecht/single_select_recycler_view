package com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list

import androidx.fragment.app.Fragment
import com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.view.FullModifiedFragment

class FullModifiedInputModule : FullModifiedInputModuleContract {

    override fun createFragment(): Fragment {
        return FullModifiedFragment.createInstance()
    }
}