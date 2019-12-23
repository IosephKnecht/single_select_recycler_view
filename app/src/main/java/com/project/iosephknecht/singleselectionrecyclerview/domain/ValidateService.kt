package com.project.iosephknecht.singleselectionrecyclerview.domain

import io.reactivex.Single

/**
 * Contract for validate service.
 *
 * @author IosephKnecht.
 */
interface ValidateService {
    fun validate(value: String): Single<Boolean>
}