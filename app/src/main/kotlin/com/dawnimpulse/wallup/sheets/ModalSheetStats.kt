/*
ISC License

Copyright 2018, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/
package com.dawnimpulse.wallup.sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.toast
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.models.UnsplashModel
import com.dawnimpulse.wallup.pojo.ImageStatsData
import com.dawnimpulse.wallup.pojo.ImageStatsPojo
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.Colors
import com.dawnimpulse.wallup.utils.L
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.bottom_sheet_statistics.*


/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-12-09 by Saksham
 *
 * @note Updates :
 */
class ModalSheetStats : RoundedBottomSheetDialogFragment() {
    private val NAME = "ModalSheetStats"
    private lateinit var model: UnsplashModel

    // on create
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_statistics, container, false);

    }

    // view created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = UnsplashModel(lifecycle)
        model.imageStats(arguments!!.getString(C.ID)) { e, r ->
            e?.let {
                L.d(NAME, e)
                context!!.toast("error fetching stats")
            }
            r?.let {
                setDownloads((r as ImageStatsPojo).downloads.historical.values)
            }
        }
    }

    // set downloads chart
    private fun setDownloads(details: List<ImageStatsData>) {
        val entries = ArrayList<Entry>()
        details.forEachIndexed { i, data ->
            entries.add(Entry(i.toFloat(), data.value.toFloat()))
        }

        val dataset = LineDataSet(entries, "Label")
        dataset.color = Colors(context!!).WHITE
        dataset.valueTextColor = Colors(context!!).WHITE

        val lineData = LineData(dataset)
        statsDownloadChart.data = lineData
        statsDownloadChart.invalidate()
    }

}