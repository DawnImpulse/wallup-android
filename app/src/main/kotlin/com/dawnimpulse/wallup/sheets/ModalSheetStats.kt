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
import android.view.animation.AnimationUtils
import androidx.core.widget.toast
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.models.UnsplashModel
import com.dawnimpulse.wallup.pojo.ImageStatsData
import com.dawnimpulse.wallup.pojo.ImageStatsPojo
import com.dawnimpulse.wallup.utils.*
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.bottom_sheet_statistics.*
import java.util.*


/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-12-09 by Saksham
 *
 * @note Updates :
 */
class ModalSheetStats : RoundedBottomSheetDialogFragment(), View.OnClickListener {
    private val NAME = "ModalSheetStats"
    private lateinit var model: UnsplashModel
    private var color = 0

    // on create
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_statistics, container, false);
    }

    // view created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = UnsplashModel(lifecycle)
        color = arguments!!.getInt(C.COLOR)

        imageStats()
        sheetStatsReload.setOnClickListener(this)
        sheetStatsProgressI.animation = AnimationUtils.loadAnimation(context, R.anim.rotation_progress)
    }

    // on click
    override fun onClick(v: View) {
        sheetStatsReload.gone()
        sheetStatsProgress.show()
        imageStats()
    }

    // fetch image stats
    private fun imageStats() {
        model.imageStats(arguments!!.getString(C.ID)) { e, r ->
            e?.let {
                L.d(NAME, e)
                sheetStatsProgress.gone()
                sheetStatsReload.show()
                context!!.toast("error fetching stats")
            }
            r?.let {
                r as ImageStatsPojo
                sheetStatsProgress.gone()
                sheetStatsReload.gone()
                sheetStatsLayout.show()
                sheetStatsDate.text = F.statsDate()
                sheetStatsDate.setTextColor(color)
                setViews(r.views.historical.values)
                setDownloads(r.downloads.historical.values)
                setLikes(r.likes.historical.values)
            }
        }
    }

    // set views chart
    private fun setViews(details: List<ImageStatsData>) {
        val min = details.minBy { it.value }!!.value

        val entries = ArrayList<Entry>()
        details.forEachIndexed { i, data ->
            entries.add(Entry(i.toFloat(), data.value.toFloat()))
        }

        val dataset = LineDataSet(entries, "Label")
        dataset.color = color
        dataset.valueTextColor = Colors(context!!).WHITE
        dataset.isHighlightEnabled = false
        dataset.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        dataset.setDrawCircles(false)


        val lineData = LineData(dataset)
        lineData.setDrawValues(false)

        val description = Description()
        description.text = ""

        statsViewsChart.data = lineData
        statsViewsChart.description = description
        statsViewsChart.xAxis.setDrawAxisLine(false)
        statsViewsChart.xAxis.setDrawLabels(true)
        statsViewsChart.xAxis.setDrawGridLines(false);    //X axis grid lines

        statsViewsChart.axisLeft.textColor = color
        statsViewsChart.axisLeft.setDrawAxisLine(false)
        statsViewsChart.axisLeft.setLabelCount(2, true)
        statsViewsChart.axisLeft.setDrawGridLines(false)
        statsViewsChart.axisLeft.axisMinimum = min.toFloat()
        statsViewsChart.axisRight.isEnabled = false;

        statsViewsChart.legend.isEnabled = false;

        val xAxis = statsViewsChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        statsViewsChart.invalidate()
    }

    // set downloads chart
    private fun setDownloads(details: List<ImageStatsData>) {
        val min = details.minBy { it.value }!!.value
        val entries = ArrayList<Entry>()
        details.forEachIndexed { i, data ->
            entries.add(Entry(i.toFloat(), data.value.toFloat()))
        }

        val dataset = LineDataSet(entries, "Label")
        dataset.color = color
        dataset.valueTextColor = Colors(context!!).WHITE
        dataset.isHighlightEnabled = false
        dataset.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        dataset.setDrawCircles(false)


        val lineData = LineData(dataset)
        lineData.setDrawValues(false)

        val description = Description()
        description.text = ""

        statsDownloadChart.data = lineData
        statsDownloadChart.description = description
        statsDownloadChart.xAxis.setDrawAxisLine(false)
        statsDownloadChart.xAxis.setDrawLabels(true)
        statsDownloadChart.xAxis.setDrawGridLines(false);    //X axis grid lines

        statsDownloadChart.axisLeft.textColor = color
        statsDownloadChart.axisLeft.setDrawAxisLine(false)
        statsDownloadChart.axisLeft.setLabelCount(2, true)
        statsDownloadChart.axisLeft.setDrawGridLines(false)
        statsDownloadChart.axisLeft.axisMinimum = min.toFloat()
        statsDownloadChart.axisRight.isEnabled = false;

        statsDownloadChart.legend.isEnabled = false;

        val xAxis = statsDownloadChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        statsDownloadChart.invalidate()
    }

    // set likes chart
    private fun setLikes(details: List<ImageStatsData>) {
        val min = details.minBy { it.value }!!.value
        val entries = ArrayList<Entry>()
        details.forEachIndexed { i, data ->
            entries.add(Entry(i.toFloat(), data.value.toFloat()))
        }

        val dataset = LineDataSet(entries, "Label")
        dataset.color = color
        dataset.valueTextColor = Colors(context!!).WHITE
        dataset.isHighlightEnabled = false
        dataset.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        dataset.setDrawCircles(false)


        val lineData = LineData(dataset)
        lineData.setDrawValues(false)

        val description = Description()
        description.text = ""

        statsLikeChart.data = lineData
        statsLikeChart.description = description
        statsLikeChart.xAxis.setDrawAxisLine(false)
        statsLikeChart.xAxis.setDrawLabels(true)
        statsLikeChart.xAxis.setDrawGridLines(false);    //X axis grid lines

        statsLikeChart.axisLeft.textColor = color
        statsLikeChart.axisLeft.setDrawAxisLine(false)
        statsLikeChart.axisLeft.setLabelCount(2, true)
        statsLikeChart.axisLeft.setDrawGridLines(false)
        statsLikeChart.axisLeft.axisMinimum = min.toFloat()
        statsLikeChart.axisRight.isEnabled = false;

        statsLikeChart.legend.isEnabled = false;

        val xAxis = statsLikeChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        statsLikeChart.invalidate()
    }
}