package com.project.iosephknecht.singleselectionrecyclerview.domain

import com.project.iosephknecht.singleselectionrecyclerview.data.SomeCategory
import com.project.iosephknecht.singleselectionrecyclerview.data.SomeModel
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*
import kotlin.random.Random.Default.nextInt

/**
 * Implementation on [SomeModelDataSource].
 *
 * @author IosephKnecht
 */
internal class SomeModelDataSourceImpl : SomeModelDataSource {
    override fun generateSomeModelList(count: Int): Single<List<SomeModel>> {
        return Observable.fromIterable(0 until count)
            .map { index -> nextSomeModel(index) }
            .toList()
    }

    override fun generateSomeModel(index: Int): Single<SomeModel> {
        return Single.just(nextSomeModel(index))
    }

    private fun nextSomeModel(index: Int): SomeModel {
        return SomeModel(
            uuid = UUID.randomUUID(),
            value = "value $index",
            label = nextCategory()
        )
    }

    private fun nextCategory(): SomeCategory {
        val randomOrdinal = nextInt(SomeCategory.values().size)
        return SomeCategory.values()[randomOrdinal]
    }
}