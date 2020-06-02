/**
 * ISC License
 *
 * Copyright 2020, Saksham (DawnImpulse)
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
 * WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
 * OR PERFORMANCE OF THIS SOFTWARE.
 **/
package com.dawnimpulse.wallup.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.models.ModelRandom
import com.dawnimpulse.wallup.objects.ObjectIssue
import com.dawnimpulse.wallup.ui.adapters.AdapterImage
import com.dawnimpulse.wallup.utils.reusables.*
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.layout_general.*

class FragmentRandom : Fragment(R.layout.layout_general) {
    private val modelRandom: ModelRandom by activityViewModels()
    private lateinit var adapter: AdapterImage
    private var disposable = CompositeDisposable()

    /**
     * on view created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        modelRandom.getList().observe(viewLifecycleOwner, homeObserver)
        modelRandom.errors().observe(viewLifecycleOwner, errorObserver)
        disposable.add(RxBusType.subscribe { rxType(it) })

        layout_general_loading.playAnimation()

        // handling reload click
        layout_general_error_reload.setOnClickListener {
            layout_general_error_anim.pauseAnimation()
            layout_general_error_layout.gone()
            layout_general_loading.show()
            layout_general_loading.playAnimation()
            modelRandom.reload()
        }
    }

    /**
     * on destroy
     */
    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }

    /**
     * handle rx type
     */
    private fun rxType(type: RxType) {
        toast("here2")
        if (type.type == RELOAD_LIST && type.data == RELOAD.MORE.RANDOM)
            modelRandom.loadMore()
    }

    /**
     * home observer
     */
    private var homeObserver = Observer<List<Any>> {
        bindRecycler(it)
    }

    /**
     * errors observer
     */
    private val errorObserver = Observer<ObjectIssue> {
        layout_general_loading.pauseAnimation()
        layout_general_loading.gone()
        layout_general_error_layout.show()
        layout_general_error_anim.playAnimation()
    }

    /**
     * load more observer
     */
    private val loadMoreObserver = Observer<Void> {
        modelRandom.loadMore()
    }

    /**
     * bind recycler
     */
    private fun bindRecycler(list: List<Any>) {
        if (!::adapter.isInitialized) {
            adapter = AdapterImage(layout_general_recycler)
            adapter.setData(list)
            layout_general_recycler.layoutManager = LinearLayoutManager(context)
            layout_general_recycler.adapter = adapter
            adapter.onLoading().observe(viewLifecycleOwner, loadMoreObserver)

            layout_general_recycler.show()
            layout_general_loading.pauseAnimation()
            layout_general_loading.gone()
        } else
            adapter.setNewData(list)
        adapter.onLoaded()
    }
}