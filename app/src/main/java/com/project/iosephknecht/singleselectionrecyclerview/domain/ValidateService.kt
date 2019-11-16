package com.project.iosephknecht.singleselectionrecyclerview.domain

import io.reactivex.Single

interface ValidateService {
    fun validate(value: String): Single<Boolean>
}