package com.project.iosephknecht.singleselectionrecyclerview.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.iosephknecht.singleselectionrecyclerview.viewModel.MainViewModel
import com.project.iosephknecht.singleselectionrecyclerview.R
import com.project.iosephknecht.singleselectionrecyclerview.view.adapter.SelectableBinder
import com.project.iosephknecht.singleselectionrecyclerview.view.adapter.SelectableAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: SelectableAdapter

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


        findViewById<RecyclerView>(R.id.recycler_view)?.apply {
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
    }

    override fun onStart() {
        super.onStart()

        with(viewModel) {
            items.observe(this@MainActivity, Observer { items ->
                items?.also {
                    adapter.reload(it)
                }
            })
        }
    }
}
