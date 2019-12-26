package com.project.iosephknecht.singleselectionrecyclerview.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.project.iosephknecht.singleselectionrecyclerview.R
import com.project.iosephknecht.singleselectionrecyclerview.application.SingletonComponentHolder
import com.project.iosephknecht.singleselectionrecyclerview.presentation.cases.CasesInputModuleContract
import com.project.iosephknecht.singleselectionrecyclerview.presentation.cases.view.CasesFragment
import com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.FullModifiedInputModuleContract
import com.project.iosephknecht.singleselectionrecyclerview.presentation.main.model.SingleSelectionCase
import com.project.iosephknecht.singleselectionrecyclerview.presentation.only_selection.OnlySelectionInputModule
import com.project.iosephknecht.singleselectionrecyclerview.presentation.partial_modified_list.PartialModifiedInputModuleContract
import com.project.iosephknecht.singleselectionrecyclerview.presentation.requestApplicationAs
import com.project.iosephknecht.singleselectionrecyclerview.presentation.visible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*
import javax.inject.Inject

/**
 * Host - activity for app.
 *
 * @author IosephKnecht.
 */
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

    @set:Inject
    protected var partialModifiedInputModule: PartialModifiedInputModuleContract? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestApplicationAs<SingletonComponentHolder>()
            .getSingletonComponentHolder()
            .mainSubComponentBuilder()
            .build()
            .inject(this)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

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

            toolbar_navigation_icon.visible(false)
            setToolbarText(null)
        }

        supportFragmentManager.addOnBackStackChangedListener {
            toolbar_navigation_icon.visible(supportFragmentManager.backStackEntryCount >= 1)
        }

        toolbar_navigation_icon.setOnClickListener { onBackPressed() }
    }

    override fun onDestroy() {
        casesInputModule = null
        onlySelectionInputModule = null
        partialModifiedInputModule = null
        fullModifiedInputModule = null

        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if (supportFragmentManager.backStackEntryCount == 0) {
            setToolbarText(null)
        }
    }

    override fun showCasesFragment(case: SingleSelectionCase) {
        val fragment = when (case) {
            SingleSelectionCase.CASE1 -> onlySelectionInputModule!!.createFragment()
            SingleSelectionCase.CASE2 -> partialModifiedInputModule!!.createFragment()
            SingleSelectionCase.CASE3 -> fullModifiedInputModule!!.createFragment()
        }

        replaceFragment(fragment)

        setToolbarText(case)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.body, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setToolbarText(case: SingleSelectionCase?) {
        val titleValue = case?.title ?: R.string.app_name
        val subtitleValue = case?.description

        toolbar_title.text = getString(titleValue)
        toolbar_subtitle.apply {
            visible(subtitleValue != null)
            text = subtitleValue?.let { getString(it) }
        }
    }
}
