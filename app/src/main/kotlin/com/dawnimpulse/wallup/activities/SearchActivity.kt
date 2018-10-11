package com.dawnimpulse.wallup.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.adapters.RandomAdapter
import com.dawnimpulse.wallup.models.UnsplashModel
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.utils.L
import kotlinx.android.synthetic.main.activity_search.*

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-09-23 by Saksham
 *
 * @note Updates :
 */
@SuppressLint("RestrictedApi")
class SearchActivity : AppCompatActivity(), View.OnClickListener {
    private val NAME = "SearchActivity"
    private var close = true // will be false if text is present i.e. clear it
    private var text: String? = null
    private lateinit var model: UnsplashModel
    private lateinit var adapter: RandomAdapter
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
                        searchLeft.setImageDrawable(ContextCompat.getDrawable(this@SearchActivity, R.drawable.vd_left))
                    }

                }
            }

        })
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
            searchFab.id -> {
                model.randomImagesTag(text!!, callback)
            }
            searchBack.id -> finish()
        }
    }

    // back press
    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.enter_from_right, R.anim.fade_out)
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
                searchRecycler.layoutManager = LinearLayoutManager(this@SearchActivity)
                searchRecycler.adapter = RandomAdapter(lifecycle, r as List<ImagePojo?>)
                searchRecycler.visibility = View.VISIBLE
                searchFab.visibility = View.VISIBLE
                searchProgress.visibility = View.GONE
            }
        }
    }
}
