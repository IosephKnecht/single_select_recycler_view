package com.project.iosephknecht.singleselectionrecyclerview.presentation.cases

import androidx.fragment.app.Fragment

/**
 * A contract for access to module with a list of cases.
 *
 * @author IosephKnecht
 */
interface CasesInputModuleContract {
    /**
     * Create fragment with a list of cases.
     */
    fun createFragment(): Fragment
}