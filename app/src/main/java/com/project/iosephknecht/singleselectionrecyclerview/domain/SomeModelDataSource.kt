package com.project.iosephknecht.singleselectionrecyclerview.domain

import com.project.iosephknecht.singleselectionrecyclerview.data.SomeModel
import io.reactivex.Single

/**
 * Contract for provider data.
 *
 * @author IosephKnecht
 */
interface SomeModelDataSource {
    /**
     * Generate list.
     *
     * @param count size of list
     */
    fun generateSomeModelList(count: Int): Single<List<SomeModel>>

    /**
     * Generate own model.
     *
     * @param index
     */
    fun generateSomeModel(index: Int): Single<SomeModel>
}