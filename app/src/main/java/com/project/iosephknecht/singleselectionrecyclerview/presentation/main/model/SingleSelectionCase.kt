package com.project.iosephknecht.singleselectionrecyclerview.presentation.main.model

import androidx.annotation.StringRes
import com.project.iosephknecht.singleselectionrecyclerview.R

enum class SingleSelectionCase(
    @StringRes val title: Int,
    @StringRes val description: Int
) {
    CASE1(
        title = R.string.single_selection_case_1_title,
        description = R.string.single_selection_case_1_description
    ),
    CASE2(
        title = R.string.single_selection_case_2_title,
        description = R.string.single_selection_case_2_description
    ),
    CASE3(
        title = R.string.single_selection_case_3_title,
        description = R.string.single_selection_case_3_description
    )
}