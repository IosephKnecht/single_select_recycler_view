package com.project.iosephknecht.singleselectionrecyclerview.presentation.only_selection.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.project.iosephknecht.singleselectionrecyclerview.domain.SomeModelDataSource
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.PerFeatureLayerScope
import com.project.iosephknecht.singleselectionrecyclerview.presentation.only_selection.contract.OnlySelectionContract
import com.project.iosephknecht.singleselectionrecyclerview.presentation.only_selection.view.OnlySelectionFragment
import com.project.iosephknecht.singleselectionrecyclerview.presentation.only_selection.viewModel.OnlySelectionViewModel
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Inject

@PerFeatureLayerScope
@Subcomponent(modules = [OnlySelectionComponentModule::class])
interface OnlySelectionComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun with(fragment: Fragment): Builder

        fun build(): OnlySelectionComponent
    }

    fun inject(fragment: OnlySelectionFragment)
}

@Module
internal class OnlySelectionComponentModule {

    @Provides
    fun provideViewModel(
        fragment: Fragment,
        factory: OnlySelectionViewModelFactory
    ): OnlySelectionContract.ViewModel {
        return ViewModelProviders.of(fragment, factory).get(OnlySelectionViewModel::class.java)
    }
}

internal class OnlySelectionViewModelFactory @Inject constructor(
    private val someModelDataSource: SomeModelDataSource
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return OnlySelectionViewModel(
            someModelDataSource = someModelDataSource
        ) as T
    }
}