package com.project.iosephknecht.singleselectionrecyclerview.presentation.cases.view

import android.content.Context
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
import com.project.iosephknecht.singleselectionrecyclerview.presentation.cases.contract.CasesContract
import com.project.iosephknecht.singleselectionrecyclerview.presentation.cases.view.adapter.CasesAdapter
import com.project.iosephknecht.singleselectionrecyclerview.presentation.main.model.SingleSelectionCase
import com.project.iosephknecht.singleselectionrecyclerview.presentation.requestApplicationAs
import kotlinx.android.synthetic.main.fragment_cases.*
import javax.inject.Inject

class CasesFragment : Fragment() {

    companion object {
        fun createInstance() = CasesFragment()
    }

    @set:Inject
    protected var viewModel: CasesContract.ViewModel? = null

    @set:Inject
    protected var host: Host? = null

    private var casesAdapter: CasesAdapter? = null

    interface Host {
        fun showCasesFragment(case: SingleSelectionCase)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        requestApplicationAs<SingletonComponentHolder>()
            .getSingletonComponentHolder()
            .singleSelectionCasesSubComponentBuilder()
            .with(this)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cases, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        casesAdapter = CasesAdapter(host!!::showCasesFragment)

        recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@CasesFragment.casesAdapter
            setHasFixedSize(false)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    override fun onStart() {
        super.onStart()

        with(viewModel!!) {
            items.observe(viewLifecycleOwner, Observer { items ->
                items?.also { casesAdapter!!.reload(it) }
            })
        }
    }

    override fun onDetach() {
        viewModel = null
        host = null

        super.onDetach()
    }

    override fun onDestroyView() {
        casesAdapter = null
        super.onDestroyView()
    }
}