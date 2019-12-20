package com.project.iosephknecht.singleselectionrecyclerview.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.project.iosephknecht.singleselectionrecyclerview.R
import com.project.iosephknecht.singleselectionrecyclerview.application.SingletonComponentHolder
import com.project.iosephknecht.singleselectionrecyclerview.presentation.cases.CasesInputModuleContract
import com.project.iosephknecht.singleselectionrecyclerview.presentation.cases.view.CasesFragment
import com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.FullModifiedInputModuleContract
import com.project.iosephknecht.singleselectionrecyclerview.presentation.only_selection.OnlySelectionInputModule
import com.project.iosephknecht.singleselectionrecyclerview.presentation.requestApplicationAs
import javax.inject.Inject

class MainActivity : AppCompatActivity(), CasesFragment.Host {

    companion object {
        private const val CASES_FRAGMENT_TAG = "CASES_FRAGMENT_TAG"
    }

    @set:Inject
    protected var casesInputModule: CasesInputModuleContract? = null

    @set:Inject
    protected var onlySelectionInputModule: OnlySelectionInputModule? = null

    @set:Inject
    protected var fullModifiedInputModule: FullModifiedInputModuleContract? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestApplicationAs<SingletonComponentHolder>()
            .getSingletonComponentHolder()
            .mainSubComponentBuilder()
            .build()
            .inject(this)

        supportFragmentManager.findFragmentByTag(CASES_FRAGMENT_TAG).also { fragment ->
            if (fragment == null) {
                supportFragmentManager.beginTransaction()
                    .add(
                        R.id.body,
                        casesInputModule!!.createFragment(),
                        CASES_FRAGMENT_TAG
                    )
                    .commit()
            }
        }
    }

    override fun onDestroy() {
        casesInputModule = null

        super.onDestroy()
    }

    override fun showCase1Fragment() {
        replaceFragment(onlySelectionInputModule!!.createFragment())
    }

    override fun showCase2Fragment() {
    }

    override fun showCase3Fragment() {
        replaceFragment(fullModifiedInputModule!!.createFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.body, fragment)
            .addToBackStack(null)
            .commit()
    }
}
