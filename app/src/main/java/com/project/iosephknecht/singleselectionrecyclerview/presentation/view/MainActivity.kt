package com.project.iosephknecht.singleselectionrecyclerview.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.project.iosephknecht.singleselectionrecyclerview.presentation.viewModel.MainViewModel
import com.project.iosephknecht.singleselectionrecyclerview.R
import com.project.iosephknecht.singleselectionrecyclerview.presentation.contract.MainContract
import com.project.iosephknecht.singleselectionrecyclerview.presentation.view.adapter.SelectableBinder
import com.project.iosephknecht.singleselectionrecyclerview.presentation.view.adapter.SelectableAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainContract.ViewModel

    private var recyclerView: RecyclerView? = null
    private var adapter: SelectableAdapter? = null
    private var floatingActionButton: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        adapter = SelectableAdapter(
            SelectableBinder { uuid ->
                viewModel.select(
                    uuid
                )
            })


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
}
