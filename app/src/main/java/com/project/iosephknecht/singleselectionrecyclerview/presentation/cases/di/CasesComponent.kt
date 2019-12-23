package com.project.iosephknecht.singleselectionrecyclerview.presentation.cases.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.project.iosephknecht.singleselectionrecyclerview.presentation.cases.contract.CasesContract
import com.project.iosephknecht.singleselectionrecyclerview.presentation.cases.view.CasesFragment
import com.project.iosephknecht.singleselectionrecyclerview.presentation.cases.viewModel.CasesViewModel
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.PerFeatureLayerScrope
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Inject

/**
 * Contract for DI - container.
 *
 * @author IosephKnecht
 */
@PerFeatureLayerScrope
@Subcomponent(modules = [CasesComponentModule::class])
interface CasesComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun with(fragment: Fragment): Builder

        fun build(): CasesComponent
    }

    fun inject(fragment: CasesFragment)
}

@Module
internal class CasesComponentModule {

    @Provides
    fun provideViewModel(
        fragment: Fragment,
        factory: CasesViewModelFactory
    ): CasesContract.ViewModel {
        return ViewModelProviders.of(fragment, factory).get(CasesViewModel::class.java)
    }

    @Provides
    fun provideHost(fragment: Fragment): CasesFragment.Host {
        val host = when {
            fragment.activity is CasesFragment.Host -> fragment.activity as CasesFragment.Host
            fragment.parentFragment is CasesFragment.Host -> fragment.parentFragment as CasesFragment.Host
            else -> null
        }
        return host!!
    }
}

@PerFeatureLayerScrope
internal class CasesViewModelFactory @Inject constructor() : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CasesViewModel() as T
    }
}