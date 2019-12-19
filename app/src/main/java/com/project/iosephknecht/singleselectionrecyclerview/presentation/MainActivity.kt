package com.project.iosephknecht.singleselectionrecyclerview.presentation

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.project.iosephknecht.singleselectionrecyclerview.R
import com.project.iosephknecht.singleselectionrecyclerview.domain.FrivolousValidateService
import com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.contract.MainContract
import com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.view.adapter.SelectableAdapter
import com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.view.adapter.SelectableBinder
import com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.view.adapter.ValidateBinder
import com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.viewModel.MainViewModel
import com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.viewModel.MainViewModelFactory
import com.project.iosephknecht.singleselectionrecyclerview.presentation.full_modified_list.viewModel.SelectableViewState
import com.project.iosephknecht.singleselectionrecyclerview.presentation.only_selection.view.OnlySelectionFragment

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainContract.ViewModel

    private var recyclerView: RecyclerView? = null
    private var adapter: SelectableAdapter? = null
    private var floatingActionButton: FloatingActionButton? = null

    companion object {
        private const val ONLY_SELECTION_FRAGMENT_TAG = "ONLY_SELECTION_FRAGMENT_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        supportFragmentManager.findFragmentByTag(ONLY_SELECTION_FRAGMENT_TAG).also { fragment ->
            if (fragment == null) {
                supportFragmentManager.beginTransaction()
                    .add(
                        R.id.body,
                        OnlySelectionFragment.createInstance(),
                        ONLY_SELECTION_FRAGMENT_TAG
                    )
                    .commit()
            }
        }

//        val factory = MainViewModelFactory(FrivolousValidateService())
//
//        viewModel = ViewModelProviders.of(this, factory).get(MainViewModel::class.java)
//
//        adapter = SelectableAdapter(
//            selectableBinder = SelectableBinder(
//                selectableColor = R.color.accent,
//                unselectableColor = android.R.color.white,
//                selectedTranslationZ = 20f,
//                unselectedTranslationZ = 0f,
//                selectableAction = viewModel::select,
//                removeAction = viewModel::remove,
//                applyChangesAction = viewModel::applyChanges
//            ),
//            validateBinder = ValidateBinder(
//                defaultBackground = R.drawable.bg_edittext_border,
//                invalidBackground = R.drawable.bg_edittext_error
//            )
//        )
//
//        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)?.apply {
//            layoutManager = LinearLayoutManager(context)
//            adapter = this@MainActivity.adapter
//            setHasFixedSize(false)
//            addItemDecoration(
//                DividerItemDecoration(
//                    context,
//                    DividerItemDecoration.VERTICAL
//                )
//            )
//            itemAnimator?.apply {
//                this as SimpleItemAnimator
//                this.supportsChangeAnimations = false
//            }
//        }
//
//        floatingActionButton = findViewById<FloatingActionButton>(R.id.floating_action_button)
    }

//    override fun onStart() {
//        super.onStart()
//
//        with(viewModel) {
//            items.observe(this@MainActivity, Observer { items ->
//                items?.also {
//                    adapter!!.reload(it)
//                }
//            })
//            addState.observe(this@MainActivity, Observer { isAdded ->
//                isAdded?.also { handleIsAddedChange(it) }
//            })
//            diff.observe(this@MainActivity, Observer { diff ->
//                diff?.also { adapter!!.applyDiff(it) }
//            })
//            confirmRemoveDialog.observe(this@MainActivity, Observer { removeModel ->
//                removeModel?.also {
//                    showRemoveConfirmDialog(it)
//                }
//            })
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//
//        recyclerView = null
//        adapter = null
//        floatingActionButton = null
//    }
//
//    private fun handleIsAddedChange(isAdded: Boolean) {
//        if (isAdded) {
//            with(recyclerView!!) {
//                val lastPosition = adapter!!.itemCount
//
//                if (lastPosition > 0) {
//                    scrollToPosition(lastPosition - 1)
//                }
//            }
//
//            floatingActionButton!!.apply {
//                setImageDrawable(
//                    ContextCompat.getDrawable(
//                        this@MainActivity,
//                        R.drawable.ic_done_black_24dp
//                    )
//                )
//                setOnClickListener { viewModel.confirmAdd() }
//            }
//        } else {
//            floatingActionButton!!.apply {
//                setImageDrawable(
//                    ContextCompat.getDrawable(
//                        this@MainActivity,
//                        R.drawable.ic_plus_one_black_24dp
//                    )
//                )
//                setOnClickListener { viewModel.add() }
//            }
//        }
//    }
//
//    private fun showRemoveConfirmDialog(viewState: SelectableViewState) {
//        val dialog = AlertDialog.Builder(this)
//            .setTitle(getString(R.string.remove_confirm_title, viewState.changedLabel))
//            .setPositiveButton(android.R.string.yes) { dialog, _ ->
//                viewModel.confirmRemove()
//                dialog.dismiss()
//            }
//            .setNegativeButton(android.R.string.no) { dialog, _ ->
//                viewModel.declineRemove()
//                dialog.dismiss()
//            }
//            .setCancelable(false)
//            .create()
//
//        dialog.show()
//    }
}
