package com.project.iosephknecht.singleselectionrecyclerview.application

/**
 * Contract to access singleton component in Application.
 *
 * @author IosephKnecht
 */
interface SingletonComponentHolder {
    fun getSingletonComponentHolder(): SingletonComponent
}