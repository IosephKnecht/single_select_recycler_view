package com.project.iosephknecht.singleselectionrecyclerview.domain

import io.reactivex.Single
import java.util.concurrent.TimeUnit
import kotlin.random.Random.Default.nextBoolean
import kotlin.random.Random.Default.nextLong

/**
 * Implementation on [ValidateService].
 *
 * @author IosephKnecht
 */
internal class FrivolousValidateService : ValidateService {
    override fun validate(value: String): Single<Boolean> {
        return Single.just(value)
            .flatMap {
                if (nextBoolean()) {
                    longValidate(it)
                } else {
                    stringValidate(it)
                }
            }
    }

    private fun longValidate(value: String): Single<Boolean> {
        return Single.just(value)
            .map { it.toLongOrNull() != null }
            .delay(nextLong(1, 2), TimeUnit.SECONDS)
    }

    private fun stringValidate(value: String): Single<Boolean> {
        return Single.just(value)
            .map { value.length in 0..10 }
            .delay(nextLong(1, 2), TimeUnit.SECONDS)
    }
}