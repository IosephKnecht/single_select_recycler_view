package com.project.iosephknecht.singleselectionrecyclerview.presentation.cases

import androidx.fragment.app.Fragment
import com.project.iosephknecht.singleselectionrecyclerview.presentation.cases.view.CasesFragment

/**
 * Implementation [CasesInputModuleContract].
 *
 * @author IosephKnecht
 */
class CasesInputModule : CasesInputModuleContract {

    override fun createFragment(): Fragment {
        return CasesFragment.createInstance()
    }
}