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
package com.dawnimpulse.wallup.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.adapter.AdapterImage
import com.dawnimpulse.wallup.utils.functions.F
import com.dawnimpulse.wallup.utils.functions.gone
import com.dawnimpulse.wallup.utils.reusables.CACHED
import kotlinx.android.synthetic.main.activity_cache.*
import org.apache.commons.io.comparator.LastModifiedFileComparator
import java.io.File
import java.util.*

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-09-02 by Saksham
 * @note Updates :
 *  Saksham - 2019 09 04 - develop - remove duplicates
 */
class CacheActivity : AppCompatActivity() {

    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cache)

        F.removeDuplicates(filesDir.listFiles().toList())

        val files = File(filesDir, CACHED).listFiles().filter { it.name.contains(".jpg") }.distinctBy { F.calculateMD5(it) }.toTypedArray()
        Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE)

        if (files.isNotEmpty()) {
            noCacheText.gone()
            recycler.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            recycler.adapter = AdapterImage(files.toList())
        }
    }
}