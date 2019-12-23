package com.project.iosephknecht.singleselectionrecyclerview.presentation.only_selection.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.iosephknecht.singleselectionrecyclerview.R
import com.project.iosephknecht.singleselectionrecyclerview.application.SingletonComponentHolder
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.delegates.*
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.ui.CustomEditTextView
import com.project.iosephknecht.singleselectionrecyclerview.presentation.disableItemChangeAnimation
import com.project.iosephknecht.singleselectionrecyclerview.presentation.getFloatDimension
import com.project.iosephknecht.singleselectionrecyclerview.presentation.only_selection.contract.OnlySelectionContract
import com.project.iosephknecht.singleselectionrecyclerview.presentation.requestApplicationAs
import kotlinx.android.synthetic.main.fragment_only_selection.*
import javax.inject.Inject

class OnlySelectionFragment : Fragment() {

    companion object {
        fun createInstance() = OnlySelectionFragment()
    }

    @set:Inject
    protected var viewModel: OnlySelectionContract.ViewModel? = null

    private var adapter: OnlySelectionAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestApplicationAs<SingletonComponentHolder>().getSingletonComponentHolder()
            .onlySelectionSubComponentBuilder()
            .with(this)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_only_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = OnlySelectionAdapter(
            selectableBackgroundDelegate = SelectableBackgroundDelegate(
                selectableBackground = R.color.accent,
                unselectableBackground = android.R.color.white
            ),
            selectableTranslationDelegate = SelectableTranslationDelegate(
                unselectedTranslationZ = resources.getFloatDimension(R.dimen.defaultTranslationZ),
                selectedTranslationZ = resources.getFloatDimension(R.dimen.selectableTranslationZ)
            ),
            selectableClickManagerDelegate = SelectableClickManagerDelegate(
                selectableAction = viewModel!!::select,
                applyChangesAction = null,
                removeAction = null
            ),
            selectableValueDelegate = SelectableValueDelegate(
                defaultState = CustomEditTextView.State.ONLY_READABLE
            ),
            selectableCategoryDelegate = SelectableCategoryDelegate()
        )

        recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@OnlySelectionFragment.adapter
            setHasFixedSize(false)
            addItemDecoration(
                DividerItemDecoration(
                    context, DividerItemDecoration.VERTICAL
                )
            )
            disableItemChangeAnimation()
        }
    }

    override fun onStart() {
        super.onStart()

        with(viewModel!!) {
            items.observe(viewLifecycleOwner, Observer { items ->
                items?.also { adapter!!.reload(it) }
            })

            changedItems.observe(viewLifecycleOwner, Observer { changedItems ->
                changedItems?.also { adapter!!.applyChanges(it) }
            })
        }
    }

    override fun onDestroyView() {
        viewModel = null
        adapter = null
        super.onDestroyView()
    }
}