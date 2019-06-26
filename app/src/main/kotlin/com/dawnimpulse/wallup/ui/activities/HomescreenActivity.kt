package com.dawnimpulse.wallup.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.adapter.HomeAdapter
import com.dawnimpulse.wallup.ui.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.ui.models.WallupViewModel
import com.dawnimpulse.wallup.ui.objects.EditorialObject
import com.dawnimpulse.wallup.ui.objects.ExploreObject
import com.dawnimpulse.wallup.utils.functions.loge
import com.dawnimpulse.wallup.utils.functions.toast
import com.dawnimpulse.wallup.utils.functions.toastd
import com.dawnimpulse.wallup.utils.handlers.ImageHandler
import com.dawnimpulse.wallup.utils.reusables.Config
import kotlinx.android.synthetic.main.activity_homescreen.*


class HomescreenActivity : AppCompatActivity(), OnLoadMoreListener {
    private lateinit var adapter: HomeAdapter
    private lateinit var model: WallupViewModel
    private lateinit var list: MutableList<Any?>
    private lateinit var backgrounds: List<String>

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

        model = WallupViewModel(this)

        // set first page of homescreen
        setHomescreen()

        // get homescreen
        model.getHomescreen { e, r ->
            e?.let {
                loge(e)
                toastd("error fetching homescreen")
            }
            r?.let {
                backgrounds = it.background
                // homescreen background
                ImageHandler.setImageOnHomescreenBackground(homescreenFrame, backgrounds[0])

                // cache other images
                /*backgrounds.asSequence().withIndex().filter { it.index > 0 }.forEach {
                    ImageHandler.cacheHomescreenImage(applicationContext, it.value)
                }*/

                // remove loading
                list.asSequence().withIndex().filter { it.value == null }.map { it.index }.forEach {
                    list.removeAt(it)
                    adapter.notifyItemRemoved(it)
                }

                // set editorial & explore in list
                list.add(EditorialObject(it.background[1], it.tags, it.images))
                list.add(ExploreObject(it.background[2], it.collections))
                adapter.notifyItemRangeInserted(1, 2)
            }
        }

        /*fetch {
            list = it.toMutableList()
            recyclerDemo.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            adapter = HomeAdapter(list, recyclerDemo)
            recyclerDemo.adapter = adapter
            adapter.setOnLoadMoreListener(this)
        }*/

    }

    /**
     * clearing recycler disposables
     */
    override fun onDestroy() {
        super.onDestroy()

        Config.disposableHomescreenActivity.clear()
    }


    // ---------------
    //    load more
    // ---------------
    override fun onLoadMore() {
        toast("loading")
        /*fetch {
            val pos = list.size
            list.addAll(it)
            adapter.notifyItemRangeInserted(pos, it.count())
            adapter.onLoaded()
            toast("loaded")
        }*/
    }


    /**
     * set homescreen's first screen
     */
    private fun setHomescreen() {
        list = mutableListOf(1, null)
        adapter = HomeAdapter(list, homescreenRecycler)
        homescreenRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        homescreenRecycler.adapter = adapter
    }
}
