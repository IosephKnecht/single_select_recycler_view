package com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.project.iosephknecht.singleselectionrecyclerview.domain.SomeModelDataSource
import com.project.iosephknecht.singleselectionrecyclerview.domain.ValidateService
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.PerFeatureLayerScrope
import com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.contract.FullModifiedContract
import com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.view.FullModifiedFragment
import com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.viewModel.FullModifiedViewModel
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Inject

@PerFeatureLayerScrope
@Subcomponent(modules = [FullModifiedComponentModule::class])
interface FullModifiedComponent {
    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun with(fragment: Fragment): Builder

        fun build(): FullModifiedComponent
    }

    fun inject(fragment: FullModifiedFragment)
}

@Module
internal class FullModifiedComponentModule {

    @Provides
    fun provideViewModel(
        fragment: Fragment,
        factory: FullModifiedViewModelFactory
    ): FullModifiedContract.ViewModel {
        return ViewModelProviders.of(fragment, factory).get(FullModifiedViewModel::class.java)
    }
}

@PerFeatureLayerScrope
internal class FullModifiedViewModelFactory @Inject constructor(
    private val someModelDataSource: SomeModelDataSource,
    private val validateService: ValidateService
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FullModifiedViewModel(
            someModelDataSource = someModelDataSource,
            validateService = validateService
        ) as T
    }
}