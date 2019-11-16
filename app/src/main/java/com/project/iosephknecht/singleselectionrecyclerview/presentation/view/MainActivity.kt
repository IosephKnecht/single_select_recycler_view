package com.project.iosephknecht.singleselectionrecyclerview.presentation.view

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
import com.project.iosephknecht.singleselectionrecyclerview.presentation.contract.MainContract
import com.project.iosephknecht.singleselectionrecyclerview.presentation.view.adapter.SelectableAdapter
import com.project.iosephknecht.singleselectionrecyclerview.presentation.view.adapter.SelectableBinder
import com.project.iosephknecht.singleselectionrecyclerview.presentation.viewModel.MainViewModel
import com.project.iosephknecht.singleselectionrecyclerview.presentation.viewModel.MainViewModelFactory
import com.project.iosephknecht.singleselectionrecyclerview.presentation.viewModel.SelectableViewState

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainContract.ViewModel

    private var recyclerView: RecyclerView? = null
    private var adapter: SelectableAdapter? = null
    private var floatingActionButton: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val factory = MainViewModelFactory(FrivolousValidateService())

        viewModel = ViewModelProviders.of(this, factory).get(MainViewModel::class.java)

        adapter = SelectableAdapter(
            selectableBinder = SelectableBinder(
                selectableColor = R.color.accent,
                unselectableColor = android.R.color.white,
                selectedTranslationZ = 20f,
                selectableAction = viewModel::select,
                removeAction = viewModel::remove,
                applyChangesAction = viewModel::applyChanges
            )
        )

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@MainActivity.adapter
            setHasFixedSize(false)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
            itemAnimator?.apply {
                this as SimpleItemAnimator
                this.supportsChangeAnimations = false
            }
        }

        floatingActionButton = findViewById<FloatingActionButton>(R.id.floating_action_button)
    }

    override fun onStart() {
        super.onStart()

        with(viewModel) {
            items.observe(this@MainActivity, Observer { items ->
                items?.also {
                    adapter!!.reload(it)
                }
            })
            addState.observe(this@MainActivity, Observer { isAdded ->
                isAdded?.also { handleIsAddedChange(it) }
            })
            diff.observe(this@MainActivity, Observer { diff ->
                diff?.also { adapter!!.applyDiff(it) }
            })
            confirmRemoveDialog.observe(this@MainActivity, Observer { removeModel ->
                removeModel?.also {
                    showRemoveConfirmDialog(it)
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        recyclerView = null
        adapter = null
        floatingActionButton = null
    }

    private fun handleIsAddedChange(isAdded: Boolean) {
        if (isAdded) {
            with(recyclerView!!) {
                val lastPosition = adapter!!.itemCount

                if (lastPosition > 0) {
                    scrollToPosition(lastPosition - 1)
                }
            }

            floatingActionButton!!.apply {
                setImageDrawable(
                    ContextCompat.getDrawable(
                        this@MainActivity,
                        R.drawable.ic_done_black_24dp
                    )
                )
                setOnClickListener { viewModel.confirmAdd() }
            }
        } else {
            floatingActionButton!!.apply {
                setImageDrawable(
                    ContextCompat.getDrawable(
                        this@MainActivity,
                        R.drawable.ic_plus_one_black_24dp
                    )
                )
                setOnClickListener { viewModel.add() }
            }
        }
    }

    private fun showRemoveConfirmDialog(viewState: SelectableViewState) {
        val dialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.remove_confirm_title, viewState.changedLabel))
            .setPositiveButton(android.R.string.yes) { dialog, _ ->
                viewModel.confirmRemove()
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.no) { dialog, _ ->
                viewModel.declineRemove()
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()

        dialog.show()
    }
}
