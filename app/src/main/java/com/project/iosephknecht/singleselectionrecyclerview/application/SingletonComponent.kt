package com.project.iosephknecht.singleselectionrecyclerview.application

import android.content.Context
import com.project.iosephknecht.singleselectionrecyclerview.domain.FrivolousValidateService
import com.project.iosephknecht.singleselectionrecyclerview.domain.SomeModelDataSource
import com.project.iosephknecht.singleselectionrecyclerview.domain.SomeModelDataSourceImpl
import com.project.iosephknecht.singleselectionrecyclerview.domain.ValidateService
import com.project.iosephknecht.singleselectionrecyclerview.presentation.only_selection.di.OnlySelectionComponent
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [SingletonComponentModule::class])
interface SingletonComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun appContext(context: Context): Builder

        fun build(): SingletonComponent
    }

    fun onlySelectionSubComponentBuilder(): OnlySelectionComponent.Builder
}

@Module
internal class SingletonComponentModule {
    @Provides
    @Singleton
    fun provideValidateService(): ValidateService {
        return FrivolousValidateService()
    }

    @Provides
    @Singleton
    fun provideSomeModelDataSource(): SomeModelDataSource {
        return SomeModelDataSourceImpl()
    }
}