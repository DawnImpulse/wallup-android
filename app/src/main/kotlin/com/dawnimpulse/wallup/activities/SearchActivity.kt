package com.dawnimpulse.wallup.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.adapters.MainAdapter
import com.dawnimpulse.wallup.models.UnsplashModel
import com.dawnimpulse.wallup.pojo.CollectionPojo
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.Colors
import com.dawnimpulse.wallup.utils.Event
import com.dawnimpulse.wallup.utils.L
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_image.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-09-23 by Saksham
 *
 * @note Updates :
 * Saksham - 2018 11 28 - master - Connection handling
 * Saksham - 2019 01 01 - master - changing random to general adapter
 */
@SuppressLint("RestrictedApi")
class SearchActivity : AppCompatActivity(), View.OnClickListener {
    private val NAME = "SearchActivity"
    private var close = true // will be false if text is present i.e. clear it
    private var text: String? = null
    private lateinit var model: UnsplashModel
    private lateinit var adapter: MainAdapter
    private lateinit var images: MutableList<ImagePojo?>

    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        model = UnsplashModel(lifecycle)

        searchLeftL.setOnClickListener(this)
        searchRightL.setOnClickListener(this)
        searchFab.setOnClickListener(this)
        searchBack.setOnClickListener(this)

        searchText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchInit(searchText.text.toString())
                true
            }
            false
        }
        searchText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    if (s.isNotEmpty()) {
                        close = false
                        searchLeft.setImageDrawable(ContextCompat.getDrawable(this@SearchActivity, R.drawable.vd_close))
                    } else {
                        close = true
                        searchLeft.setImageDrawable(ContextCompat.getDrawable(this@SearchActivity, R.drawable.vd_menu_right_outline))
                    }

                }
            }

        })
        searchProgressI.animation = AnimationUtils.loadAnimation(this, R.anim.rotation_progress)
    }

    // on start
    override fun onStart() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
        super.onStart()
    }

    // on destroy
    override fun onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    // on click
    override fun onClick(v: View) {
        when (v.id) {
            searchLeftL.id -> {
                if (close) {
                    finish()
                    overridePendingTransition(R.anim.enter_from_right, R.anim.fade_out)
                } else
                    searchText.setText("")
            }
            searchRightL.id -> searchInit(searchText.text.toString())
            searchFab.id -> searching(text!!)
            searchBack.id -> finish()
        }
    }

    // back press
    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.enter_from_right, R.anim.fade_out)
    }

    // on message event
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: Event) {
        if (event.obj.has(C.TYPE)) {
            if (event.obj.getString(C.TYPE) == C.NETWORK) {
                runOnUiThread {
                    if (event.obj.getBoolean(C.NETWORK)) {
                        searchConnLayout.setBackgroundColor(Colors(this).GREEN)
                        searchConnText.text = "Back Online"
                        launch {
                            delay(1500)
                            runOnUiThread {
                                searchConnLayout.visibility = View.GONE
                            }
                        }
                    } else {
                        searchConnLayout.visibility = View.VISIBLE
                        searchConnLayout.setBackgroundColor(Colors(this).LIKE)
                        searchConnText.text = "No Internet"
                    }
                }
            }
            if (event.obj.getString(C.TYPE) == C.LIKE) {
                // get id of the image
                val id = event.obj.getString(C.ID)
                // get position array for the image
                var position = images.asSequence().withIndex().filter { it.value!!.id == id }.map { it.index }.toList()
                // if position found
                if (position.isNotEmpty()) {
                    // change like state in images array
                    for (pos in position) {
                        L.d(NAME, pos)
                        images[pos]!!.liked_by_user = event.obj.getBoolean(C.LIKE)
                        adapter.notifyItemChanged(pos)
                    }
                }
            }
            if (event.obj.getString(C.TYPE) == C.IMAGE_TO_COLLECTION) {
                // if image is added to a collection
                if (event.obj.getBoolean(C.IS_ADDED)) {
                    var col = Gson().fromJson(event.obj.getString(C.COLLECTION), CollectionPojo::class.java)
                    var list = images
                            .asSequence()
                            .withIndex()
                            .filter { it.value!!.id == event.obj.getString(C.IMAGE) }
                            .map { it.index }
                            .toList()

                    if (list.isNotEmpty()) {
                        for (l in list) {
                            if (images[l]!!.current_user_collections == null)
                                images[l]!!.current_user_collections = arrayListOf()
                            images[l]!!.current_user_collections!!.add(col)
                            adapter.notifyItemChanged(l)
                        }
                    }
                } else {
                    //if image is removed from collection
                    var list = images
                            .asSequence()
                            .withIndex()
                            .filter { it.value!!.id == event.obj.getString(C.IMAGE) }
                            .map { it.index }
                            .toList()

                    if (list.isNotEmpty()) {
                        for (l in list) {
                            var cid = images[l]!!.current_user_collections!!
                                    .asSequence()
                                    .withIndex()
                                    .filter { it.value.id == event.obj.getString(C.COLLECTION_ID) }
                                    .map { it.index }
                                    .toList()
                            images[l]!!.current_user_collections!!.removeAt(cid[0])
                            adapter.notifyItemChanged(l)
                        }
                    }
                }
            }
        }
    }

    // initial checking before search
    private fun searchInit(text: String) {
        this.text = text
        if (text.isNotEmpty()) {
            if (text.length >= 3)
                searching(text)
            else
                toast("kindly provide at least 3 letters")

        } else
            toast("kindly provide search query")
    }

    // searching text
    private fun searching(text: String) {
        searchIl.visibility = View.GONE
        searchRecycler.visibility = View.GONE
        searchFab.visibility = View.GONE
        searchProgress.visibility = View.VISIBLE

        model.randomImagesTag(text, callback)
    }

    // callback
    private var callback = object : (Any?, Any?) -> Unit {

        override fun invoke(e: Any?, r: Any?) {
            e?.let {
                toast("error while searching")
                L.e(NAME, e)
                searchProgress.visibility = View.GONE
                searchIl.visibility = View.VISIBLE
            }
            r?.let {
                images = (r as List<ImagePojo?>).toMutableList()
                adapter = MainAdapter(lifecycle, images, searchRecycler)
                searchRecycler.layoutManager = LinearLayoutManager(this@SearchActivity)
                searchRecycler.adapter = adapter
                (searchRecycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
                searchRecycler.visibility = View.VISIBLE
                searchFab.visibility = View.VISIBLE
                searchProgress.visibility = View.GONE
            }
        }
    }
}
