package com.project.iosephknecht.singleselectionrecyclerview.presentation.main.di

import com.project.iosephknecht.singleselectionrecyclerview.presentation.cases.CasesInputModule
import com.project.iosephknecht.singleselectionrecyclerview.presentation.cases.CasesInputModuleContract
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.PerFeatureLayerScrope
import com.project.iosephknecht.singleselectionrecyclerview.presentation.main.MainActivity
import com.project.iosephknecht.singleselectionrecyclerview.presentation.only_selection.OnlySelectionInputModule
import com.project.iosephknecht.singleselectionrecyclerview.presentation.only_selection.OnlySelectionInputModuleImpl
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@PerFeatureLayerScrope
@Subcomponent(modules = [MainComponentModule::class])
interface MainComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): MainComponent
    }

    fun inject(activity: MainActivity)
}

@Module
internal class MainComponentModule {
    @Provides
    @PerFeatureLayerScrope
    fun provideCasesInputModule(): CasesInputModuleContract {
        return CasesInputModule()
    }

    @Provides
    @PerFeatureLayerScrope
    fun provideOnlySelectionInputModule(): OnlySelectionInputModule {
        return OnlySelectionInputModuleImpl()
    }
}