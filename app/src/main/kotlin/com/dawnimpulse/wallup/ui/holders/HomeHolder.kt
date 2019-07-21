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
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.activities.SettingsActivity
import com.dawnimpulse.wallup.utils.functions.openActivity
import com.dawnimpulse.wallup.utils.functions.putAny
import com.dawnimpulse.wallup.utils.reusables.AUTO_WALLPAPER_FEATURE
import com.dawnimpulse.wallup.utils.reusables.Prefs
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
import kotlinx.android.synthetic.main.inflator_home.view.*

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-17 by Saksham
 * @note Updates :
 */
class HomeHolder(view: View) : RecyclerView.ViewHolder(view) {
    val homeDown = view.homeDown
    private val info = view.homeInfo
    private val context = view.context

    fun bind() {

        // info
        info.setOnClickListener {
            context.openActivity(SettingsActivity::class.java)
        }

        if (!Prefs.contains(AUTO_WALLPAPER_FEATURE)) {
            TapTargetView.showFor(context as AppCompatActivity,
                    TapTarget.forView(info, "Auto Changing Wallpaper", "Tap to set Auto Changing Wallpaper and also view other settings & help options")
                            .outerCircleColor(R.color.colorAccent)
                            .targetCircleColor(R.color.black)
                            .titleTextColor(R.color.black)
                            .descriptionTextColor(R.color.black)
                    ,
                    object: TapTargetView.Listener() {
                        override fun onOuterCircleClick(view: TapTargetView?) {
                            super.onOuterCircleClick(view)
                            Prefs.putAny(AUTO_WALLPAPER_FEATURE,true)
                            view!!.dismiss(true)
                        }

                        override fun onTargetClick(view: TapTargetView?) {
                            super.onTargetClick(view)
                            Prefs.putAny(AUTO_WALLPAPER_FEATURE,true)
                            view!!.dismiss(true)
                        }
                    }
            )
        }


    }
}