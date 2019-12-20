package com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.iosephknecht.singleselectionrecyclerview.R
import com.project.iosephknecht.singleselectionrecyclerview.application.SingletonComponent
import com.project.iosephknecht.singleselectionrecyclerview.application.SingletonComponentHolder
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.delegates.*
import com.project.iosephknecht.singleselectionrecyclerview.presentation.common.viewState.SelectableViewState
import com.project.iosephknecht.singleselectionrecyclerview.presentation.disableItemChangeAnimation
import com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.contract.FullModifiedContract
import com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.view.adapter.FullModifiedAdapter
import com.project.iosephknecht.singleselectionrecyclerview.presentation.requestApplicationAs
import kotlinx.android.synthetic.main.fragment_full_modified.*
import javax.inject.Inject

class FullModifiedFragment : Fragment() {
    companion object {
        fun createInstance() = FullModifiedFragment()
    }

    @set:Inject
    protected var viewModel: FullModifiedContract.ViewModel? = null

    private var adapter: FullModifiedAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        requestApplicationAs<SingletonComponentHolder>()
            .getSingletonComponentHolder()
            .fullModifiedSubComponentBuilder()
            .with(this)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_full_modified, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FullModifiedAdapter(
            selectableBackgroundDelegate = SelectableBackgroundDelegate(
                unselectableBackground = android.R.color.white,
                selectableBackground = R.color.accent
            ),
            selectableClickManagerDelegate = SelectableClickManagerDelegate(
                canBeModified = true,
                selectableAction = viewModel!!::select,
                applyChangesAction = viewModel!!::applyChanges,
                removeAction = viewModel!!::remove
            ),
            selectableValueDelegate = SelectableValueDelegate(
                canBeModified = true
            ),
            selectableCategoryDelegate = SelectableCategoryDelegate(
                canBeModified = true
            ),
            selectableErrorDelegate = SelectableErrorDelegate(
                defaultBackground = R.drawable.bg_edittext_border,
                errorBackground = R.drawable.bg_edittext_error
            )
        )

        recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@FullModifiedFragment.adapter
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
                items?.also {
                    adapter!!.reload(it)
                }
            })
            addState.observe(viewLifecycleOwner, Observer { isAdded ->
                isAdded?.also { handleIsAddedChange(it) }
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

    private fun handleIsAddedChange(isAdded: Boolean) {
        if (isAdded) {
            with(recycler_view) {
                val lastPosition = adapter!!.itemCount

                if (lastPosition > 0) {
                    scrollToPosition(lastPosition - 1)
                }
            }

            floating_action_button.apply {
                setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_done_black_24dp
                    )
                )
                setOnClickListener { viewModel!!.confirmAdd() }
            }
        } else {
            floating_action_button!!.apply {
                setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_plus_one_black_24dp
                    )
                )
                setOnClickListener { viewModel!!.add() }
            }
        }
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