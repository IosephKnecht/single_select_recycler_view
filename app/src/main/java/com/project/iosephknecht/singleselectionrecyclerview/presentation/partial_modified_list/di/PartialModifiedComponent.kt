package com.project.iosephknecht.singleselectionrecyclerview.presentation.partial_modified_list.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.project.iosephknecht.singleselectionrecyclerview.domain.SomeModelDataSource
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.PerFeatureLayerScrope
import com.project.iosephknecht.singleselectionrecyclerview.presentation.partial_modified_list.contract.PartialModifiedContract
import com.project.iosephknecht.singleselectionrecyclerview.presentation.partial_modified_list.view.PartialModifiedFragment
import com.project.iosephknecht.singleselectionrecyclerview.presentation.partial_modified_list.viewModel.PartialModifiedViewModel
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Inject

@PerFeatureLayerScrope
@Subcomponent(modules = [PartialModifiedComponentModule::class])
interface PartialModifiedComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun with(fragment: Fragment): Builder

        fun build(): PartialModifiedComponent
    }

    fun inject(fragment: PartialModifiedFragment)
}

@Module
internal class PartialModifiedComponentModule {
    @Provides
    fun provideViewModel(
        fragment: Fragment,
        factory: PartialModifiedViewModelFactory
    ): PartialModifiedContract.ViewModel {
        return ViewModelProviders.of(fragment, factory).get(PartialModifiedViewModel::class.java)
    }
}

@PerFeatureLayerScrope
internal class PartialModifiedViewModelFactory @Inject constructor(
    private val someModelDataSource: SomeModelDataSource
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PartialModifiedViewModel(
            someModelDataSource = someModelDataSource
        ) as T
    }
}