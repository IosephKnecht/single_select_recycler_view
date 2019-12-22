package com.project.iosephknecht.singleselectionrecyclerview.presentation.partial_modified_list.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.iosephknecht.singleselectionrecyclerview.R
import com.project.iosephknecht.singleselectionrecyclerview.application.SingletonComponentHolder
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.delegates.SelectableBackgroundDelegate
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.delegates.SelectableCategoryDelegate
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.delegates.SelectableClickManagerDelegate
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.delegates.SelectableValueDelegate
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.viewState.SelectableViewState
import com.project.iosephknecht.singleselectionrecyclerview.presentation.disableItemChangeAnimation
import com.project.iosephknecht.singleselectionrecyclerview.presentation.partial_modified_list.contract.PartialModifiedContract
import com.project.iosephknecht.singleselectionrecyclerview.presentation.partial_modified_list.view.adapter.PartialModifiedAdapter
import com.project.iosephknecht.singleselectionrecyclerview.presentation.requestApplicationAs
import com.project.iosephknecht.singleselectionrecyclerview.presentation.scrollToLastPosition
import kotlinx.android.synthetic.main.fragment_partial_modified.*
import javax.inject.Inject

class PartialModifiedFragment : Fragment() {

    companion object {
        fun createInstance() = PartialModifiedFragment()
    }

    @set:Inject
    protected var viewModel: PartialModifiedContract.ViewModel? = null

    private var adapter: PartialModifiedAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        requestApplicationAs<SingletonComponentHolder>()
            .getSingletonComponentHolder()
            .partialModifiedSubComponentBuilder()
            .with(this)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_partial_modified, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PartialModifiedAdapter(
            selectableBackgroundDelegate = SelectableBackgroundDelegate(
                unselectableBackground = android.R.color.white,
                selectableBackground = R.color.accent
            ),
            selectableClickManagerDelegate = SelectableClickManagerDelegate(
                selectableAction = viewModel!!::select,
                removeAction = viewModel!!::remove,
                applyChangesAction = null
            ),
            selectableValueDelegate = SelectableValueDelegate(),
            selectableCategoryDelegate = SelectableCategoryDelegate()
        )

        recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@PartialModifiedFragment.adapter
            setHasFixedSize(false)
            addItemDecoration(
                DividerItemDecoration(
                    context, DividerItemDecoration.VERTICAL
                )
            )
            disableItemChangeAnimation()
        }

        floating_action_button.apply {
            setOnClickListener {
                viewModel!!.add()
                recycler_view.scrollToLastPosition()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        with(viewModel!!) {
            items.observe(viewLifecycleOwner, Observer { items ->
                items?.also {
                    adapter!!.reload(it)
                }
            })
            changedItems.observe(viewLifecycleOwner, Observer { diff ->
                diff?.also { adapter!!.applyChanges(it) }
            })
            confirmRemoveDialog.observe(viewLifecycleOwner, Observer { removeModel ->
                removeModel?.also {
                    showRemoveConfirmDialog(it)
                }
            })
        }
    }

    override fun onDestroyView() {
        adapter = null
        super.onDestroyView()
    }

    override fun onDetach() {
        viewModel = null
        super.onDetach()
    }

    private fun showRemoveConfirmDialog(viewState: SelectableViewState) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.remove_confirm_title, viewState.changedLabel))
            .setPositiveButton(android.R.string.yes) { dialog, _ ->
                viewModel!!.confirmRemove()
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.no) { dialog, _ ->
                viewModel!!.declineRemove()
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()

        dialog.show()
    }
}