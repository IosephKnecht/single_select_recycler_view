package com.project.iosephknecht.singleselectionrecyclerview.domain

import com.project.iosephknecht.singleselectionrecyclerview.data.SomeModel
import io.reactivex.Single

interface SomeModelDataSource {
    fun generateSomeModelList(count: Int): Single<List<SomeModel>>
    fun generateSomeModel(index: Int): Single<SomeModel>
}