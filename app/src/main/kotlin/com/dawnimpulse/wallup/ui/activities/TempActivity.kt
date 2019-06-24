package com.dawnimpulse.wallup.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.adapter.TempAdapter
import com.dawnimpulse.wallup.ui.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.utils.functions.toast
import kotlinx.android.synthetic.main.activity_homescreen.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class TempActivity : AppCompatActivity(), OnLoadMoreListener {
    private lateinit var adapter: TempAdapter
    private lateinit var list: MutableList<String>

    // -------------
    //    create
    // -------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homescreen)

        if (savedInstanceState == null) {
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(homescreenRecycler)
        }

        fetch {
            list = it.toMutableList()
            homescreenRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            adapter = TempAdapter(list)
            homescreenRecycler.adapter = adapter
        }
    }

    // ---------------
    //    load more
    // ---------------
    override fun onLoadMore() {
        toast("loading")
        fetch {
            val pos = list.size
            list.addAll(it)
            adapter.notifyItemRangeInserted(pos, it.count())
            toast("loaded")
        }
    }


    private fun fetch(callback: (List<String>) -> Unit) {
        GlobalScope.launch {
            Thread.sleep(2000)
            runOnUiThread {
                callback((1..10).map { it.toString() })
            }
        }
    }
}
