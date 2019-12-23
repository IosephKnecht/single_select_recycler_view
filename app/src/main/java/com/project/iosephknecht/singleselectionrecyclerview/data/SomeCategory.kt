package com.project.iosephknecht.singleselectionrecyclerview.data

import android.content.res.Resources
import androidx.annotation.StringRes
import com.project.iosephknecht.singleselectionrecyclerview.R

/**
 * Presentation model for category value.
 *
 * @author IosephKnecht
 */
enum class SomeCategory(
    @StringRes val stringRes: Int
) {
    CATEGORY1(R.string.category_1_title),
    CATEGORY2(R.string.category_2_title),
    CATEGORY3(R.string.category_3_title),
    CATEGORY4(R.string.category_4_title);

    fun getTitle(resources: Resources): String? {
        return resources.getString(stringRes)
    }
}