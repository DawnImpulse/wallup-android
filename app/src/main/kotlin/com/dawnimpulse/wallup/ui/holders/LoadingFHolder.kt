/**
 * ISC License
 *
 * Copyright 2018-2019, Saksham (DawnImpulse)
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
package com.dawnimpulse.wallup.ui.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.utils.functions.RxBus
import com.dawnimpulse.wallup.utils.functions.gone
import com.dawnimpulse.wallup.utils.functions.show
import com.dawnimpulse.wallup.utils.reusables.Config
import com.dawnimpulse.wallup.utils.reusables.FAIL_LOAD_MORE_F
import com.dawnimpulse.wallup.utils.reusables.LOAD_MORE_F
import kotlinx.android.synthetic.main.inflator_loading_full.view.*

/**
 * @info - loading for fullscreen
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-07-04 by Saksham
 * @note Updates :
 */
class LoadingFHolder (view: View):RecyclerView.ViewHolder(view){
    val progress = view.loadFP
    val loading = view.loadFL

    /**
     * loading
     */
    fun bind(){
        loading.setOnClickListener {
            progress.show()
            loading.gone()
            RxBus.accept(LOAD_MORE_F)
        }

        Config.disposableCollectionsActivity.add(
                RxBus.subscribe {
                    if(it == FAIL_LOAD_MORE_F){
                        progress.gone()
                        loading.show()
                    }
                }
        )
    }

}